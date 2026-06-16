#!/usr/bin/env bash
# ============================================================
# scripts/docker.sh — Etapa 2 do deploy
# Constrói as imagens e sobe os containers (API + MySQL),
# aguardando a API ficar disponível.
#
# Uso:
#   scripts/docker.sh           # build + up + health check
#   scripts/docker.sh --down    # derruba containers e volumes
# ============================================================

set -euo pipefail

GREEN='\033[0;32m'; YELLOW='\033[1;33m'; RED='\033[0;31m'; NC='\033[0m'
log()  { echo -e "${GREEN}[docker]${NC} $1"; }
warn() { echo -e "${YELLOW}[docker]${NC} $1"; }
err()  { echo -e "${RED}[docker]${NC} $1" >&2; }

# Raiz do projeto (um nível acima de scripts/)
cd "$(dirname "$0")/.."

ACTION="up"
for arg in "$@"; do
  case "$arg" in
    --down) ACTION="down" ;;
    *) err "Argumento desconhecido: $arg"; exit 1 ;;
  esac
done

# Detecta o comando do Docker Compose (v2 ou v1)
if docker compose version >/dev/null 2>&1; then
  COMPOSE="docker compose"
elif command -v docker-compose >/dev/null 2>&1; then
  COMPOSE="docker-compose"
else
  err "Docker Compose não encontrado. Instale o Docker Desktop ou o plugin docker-compose."
  exit 1
fi

# Pré-requisitos
command -v docker >/dev/null 2>&1 || { err "Docker não está instalado ou não está no PATH."; exit 1; }
docker info >/dev/null 2>&1 || { err "O daemon do Docker não está em execução. Inicie o Docker e tente novamente."; exit 1; }

# Ação: derrubar tudo
if [ "$ACTION" = "down" ]; then
  log "Derrubando containers e volumes..."
  $COMPOSE down -v
  log "Ambiente removido."
  exit 0
fi

# Garante o arquivo .env
if [ ! -f .env ]; then
  warn "Arquivo .env não encontrado; criando a partir de .env.example."
  cp .env.example .env
fi

# Carrega variáveis do .env (ex.: APP_PORT, credenciais do admin)
set -a; # shellcheck disable=SC1091
source .env; set +a
APP_PORT="${APP_PORT:-8080}"

log "Construindo as imagens Docker..."
$COMPOSE build

log "Subindo os containers (API + MySQL)..."
$COMPOSE up -d

log "Aguardando a API responder..."
HEALTH_URL="http://localhost:${APP_PORT}/api/v1/v3/api-docs"
ATTEMPTS=40
until curl -sf "$HEALTH_URL" >/dev/null 2>&1; do
  ATTEMPTS=$((ATTEMPTS - 1))
  if [ "$ATTEMPTS" -le 0 ]; then
    err "A API não respondeu a tempo. Verifique os logs com: $COMPOSE logs -f app"
    exit 1
  fi
  sleep 5
done

log "Aplicação no ar! 🚀"
echo ""
echo "  API base ......: http://localhost:${APP_PORT}/api/v1"
echo "  Swagger UI ....: http://localhost:${APP_PORT}/api/v1/swagger-ui.html"
echo "  OpenAPI docs ..: ${HEALTH_URL}"
echo "  Admin inicial .: ${APP_ADMIN_EMAIL:-admin@sus.local} / ${APP_ADMIN_PASSWORD:-admin123}"
echo ""
echo "  Logs ..........: $COMPOSE logs -f app"
echo "  Encerrar ......: ./deploy.sh --down"
