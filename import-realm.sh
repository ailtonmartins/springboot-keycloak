#!/bin/bash

# Iniciar o Keycloak e importar o Realm
/opt/keycloak/bin/kc.sh start-dev --import-realm

# Manter o container rodando
tail -f /dev/null
