#!/usr/bin/env bash
# ============================================================
# scripts/build.sh — Etapa 1 do deploy
# Compila, executa os testes e empacota a aplicação via
# Maven (mvn clean install).
#
# Uso:
#   scripts/build.sh             # clean install com testes
#   scripts/build.sh --no-tests  # clean install pulando os testes
# ============================================================

set -euo pipefail

GREEN='\033[0;32m'; YELLOW='\033[1;33m'; RED='\033[0;31m'; NC='\033[0m'
log()  { echo -e "${GREEN}[build]${NC} $1"; }
warn() { echo -e "${YELLOW}[build]${NC} $1"; }
err()  { echo -e "${RED}[build]${NC} $1" >&2; }

# Raiz do projeto (um nível acima de scripts/)
cd "$(dirname "$0")/.."

SKIP_TESTS=false
for arg in "$@"; do
  case "$arg" in
    --no-tests) SKIP_TESTS=true ;;
    *) err "Argumento desconhecido: $arg"; exit 1 ;;
  esac
done

# Resolve o executável do Maven (wrapper de preferência)
if [ -x ./mvnw ]; then
  MVN="./mvnw"
elif [ -f ./mvnw ]; then
  MVN="bash ./mvnw"
elif command -v mvn >/dev/null 2>&1; then
  MVN="mvn"
else
  err "Maven não encontrado (nem mvnw nem mvn no PATH)."
  exit 1
fi

if [ "$SKIP_TESTS" = true ]; then
  warn "Executando: mvn clean install (SEM testes)"
  $MVN -q -B clean install -DskipTests
else
  log "Executando: mvn clean install (com testes)"
  $MVN -q -B clean install
fi

log "Build concluído com sucesso. JAR gerado em target/."
