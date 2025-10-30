#!/bin/bash
cd /home/kavia/workspace/code-generation/personal-expense-tracker-40343-40352/smartspender_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

