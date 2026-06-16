#!/usr/bin/env bash
# ============================================================
# deploy.sh — Orquestrador de deploy da aplicação Hackathon SUS
#
# Executa, em ordem, dois sub-scripts:
#   1. scripts/build.sh  -> mvn clean install + testes
#   2. scripts/docker.sh -> build e up das imagens (API + MySQL)
#
# Uso:
#   ./deploy.sh             # build completo + subida dos containers
#   ./deploy.sh --no-tests  # pula os testes na etapa de build
#   ./deploy.sh --down      # derruba os containers e volumes
# ============================================================

set -euo pipefail

GREEN='\033[0;32m'; RED='\033[0;31m'; NC='\033[0m'
log() { echo -e "${GREEN}[deploy]${NC} $1"; }
err() { echo -e "${RED}[deploy]${NC} $1" >&2; }

cd "$(dirname "$0")"

BUILD_ARGS=()
ACTION="deploy"
for arg in "$@"; do
  case "$arg" in
    --no-tests) BUILD_ARGS+=("--no-tests") ;;
    --down)     ACTION="down" ;;
    *) err "Argumento desconhecido: $arg"; echo "Uso: ./deploy.sh [--no-tests] [--down]"; exit 1 ;;
  esac
done

chmod +x scripts/build.sh scripts/docker.sh 2>/dev/null || true

# Ação: derrubar o ambiente (delega ao script de docker)
if [ "$ACTION" = "down" ]; then
  bash scripts/docker.sh --down
  exit 0
fi

# ---- Etapa 1: build + testes ----
log "==> Etapa 1/2: build da aplicação (mvn clean install)"
bash scripts/build.sh "${BUILD_ARGS[@]}"

# ---- Etapa 2: imagens + containers ----
log "==> Etapa 2/2: subida dos containers Docker"
bash scripts/docker.sh

log "Deploy finalizado com sucesso."
