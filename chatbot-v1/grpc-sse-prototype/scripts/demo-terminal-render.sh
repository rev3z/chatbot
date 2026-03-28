#!/usr/bin/env bash
set -euo pipefail

printf '%s\n' \
'{"sessionId":"demo-terminal","message":"请帮我生成一段广告文案","endOfTurn":false}' \
'{"sessionId":"demo-terminal","message":"产品是精品咖啡豆","endOfTurn":false}' \
'{"sessionId":"demo-terminal","message":"语气要温暖一点","endOfTurn":true}' \
| curl -sN \
  -H "Content-Type: application/x-ndjson" \
  --data-binary @- \
  http://localhost:8080/api/terminal/render

printf '\n'
