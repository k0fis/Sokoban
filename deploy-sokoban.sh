#!/bin/bash
# deploy-sokoban.sh — Download latest Sokoban web build from GitHub and deploy
# Usage: ./deploy-sokoban.sh [target_dir]
# Default target: ~/www/kuba/gm004

set -e

REPO="k0fis/Sokoban"
TARGET="${1:-$HOME/www/kuba/gm004}"
TMP=$(mktemp -d)

echo "Fetching latest release from github.com/$REPO ..."
URL=$(curl -s "https://api.github.com/repos/$REPO/releases/latest" \
  | grep -o '"browser_download_url": *"[^"]*"' \
  | head -1 \
  | cut -d'"' -f4)

if [ -z "$URL" ]; then
  echo "ERROR: No release found."
  rm -rf "$TMP"
  exit 1
fi

echo "Downloading: $URL"
curl -sL "$URL" -o "$TMP/sokoban-web.tar.gz"

echo "Deploying to: $TARGET"
rm -rf "$TARGET"
mkdir -p "$TARGET"
tar -xzf "$TMP/sokoban-web.tar.gz" -C "$TARGET"

rm -rf "$TMP"
echo "Done. Deployed to $TARGET"
