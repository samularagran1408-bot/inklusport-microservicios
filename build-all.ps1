# build-all.ps1
Write-Host "🚀 Compilando todos los microservicios..." -ForegroundColor Cyan

$services = @(
    "ink-ms-auth",
    "ink-ms-users",
    "ink-ms-sports",
    "ink-ms-accessibility",
    "ink-ms-admin",
    "ink-ms-reports",
    "ink-ms-ai-assistant",
    "ink-ms-gateway"
)

foreach ($service in $services) {
    Write-Host "`n🔨 Compilando $service..." -ForegroundColor Yellow
    Push-Location $service
    mvn clean package -DskipTests
    Pop-Location
}

Write-Host "`n✅ Todos los microservicios compilados!" -ForegroundColor Green