#!/bin/bash

echo "========================================"
echo "Starting Warehouse Web Client"
echo "========================================"
echo ""
echo "Web Client will be available at: http://localhost:8090"
echo "Make sure the API Gateway is running at: http://localhost:8080"
echo ""

cd web-client
mvn spring-boot:run

