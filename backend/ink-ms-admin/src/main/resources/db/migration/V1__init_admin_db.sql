CREATE DATABASE IF NOT EXISTS admin_ms;
USE admin_ms;

-- 1. AUDITORÍA DE ACCIONES ADMINISTRATIVAS
CREATE TABLE admin_audit_log (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    admin_id CHAR(36) NOT NULL COMMENT 'ID del administrador (referencia a Users MS)',
    action VARCHAR(100) NOT NULL COMMENT 'Acción realizada (ej: USER_BLOCK, EVENT_APPROVE)',
    target_type VARCHAR(50) COMMENT 'Tipo de entidad afectada (user, sport, event, role, ai_config)',
    target_id VARCHAR(100) COMMENT 'ID de la entidad afectada',
    details JSON COMMENT 'Detalles adicionales en formato JSON',
    ip_address VARCHAR(45) COMMENT 'Dirección IP del administrador',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de la acción',
    
    INDEX idx_admin_id (admin_id),
    INDEX idx_action (action),
    INDEX idx_created_at (created_at)
);

-- 2. CONFIGURACIÓN SIMPLE DEL SISTEMA
CREATE TABLE system_config (
    config_key VARCHAR(100) PRIMARY KEY COMMENT 'Clave de configuración',
    config_value TEXT NOT NULL COMMENT 'Valor de la configuración',
    description TEXT COMMENT 'Descripción del propósito',
    updated_by CHAR(36) COMMENT 'ID del administrador que actualizó',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización'
);

-- 3. ROLES DE ADMINISTRADORES
CREATE TABLE admin_role (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único del rol',
    name VARCHAR(50) UNIQUE NOT NULL COMMENT 'Nombre del rol (SUPER_ADMIN, MODERATOR, ANALYST)',
    description TEXT COMMENT 'Descripción del rol',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación'
);

-- 4. ASIGNACIÓN DE ROLES A ADMINISTRADORES
CREATE TABLE admin_user_role (
    admin_id CHAR(36) NOT NULL COMMENT 'ID del administrador (referencia a Users MS)',
    role_id INT NOT NULL COMMENT 'ID del rol asignado',
    assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de asignación',
    assigned_by CHAR(36) COMMENT 'ID del administrador que asignó el rol',
    PRIMARY KEY (admin_id, role_id),
    FOREIGN KEY (role_id) REFERENCES admin_role(id) ON DELETE CASCADE
);

-- 5. APROBACIONES PENDIENTES (WORKFLOW DE MODERACIÓN)
CREATE TABLE pending_approval (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'Identificador único de la solicitud',
    target_type VARCHAR(50) NOT NULL COMMENT 'Tipo de entidad (event, sport, disability)',
    target_data JSON NOT NULL COMMENT 'Datos propuestos de la entidad',
    requested_by CHAR(36) NOT NULL COMMENT 'ID del administrador que solicitó',
    requested_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de solicitud',
    status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending' COMMENT 'Estado de la solicitud',
    reviewed_by CHAR(36) COMMENT 'ID del administrador que revisó',
    reviewed_at DATETIME COMMENT 'Fecha de revisión',
    review_notes TEXT COMMENT 'Notas de la revisión',
    
    INDEX idx_status (status),
    INDEX idx_target_type (target_type)
);

-- 6. CONFIGURACIÓN DE IA
CREATE TABLE ai_configuration (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único',
    feature_name VARCHAR(100) UNIQUE NOT NULL COMMENT 'Funcionalidad IA (biomechanical_analysis, fatigue_detection, injury_prediction)',
    is_enabled BOOLEAN DEFAULT TRUE COMMENT 'Indica si la funcionalidad está activa',
    model_version VARCHAR(50) COMMENT 'Versión del modelo de IA',
    confidence_threshold DECIMAL(3,2) DEFAULT 0.75 COMMENT 'Umbral de confianza mínimo (0-1)',
    parameters JSON COMMENT 'Parámetros específicos del modelo',
    updated_by CHAR(36) COMMENT 'ID del administrador que actualizó',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de actualización'
);

-- 7. ALERTAS ADMINISTRATIVAS
CREATE TABLE admin_alert (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'Identificador único',
    type VARCHAR(50) NOT NULL COMMENT 'Tipo de alerta (suspicious_activity, user_report, system_error)',
    severity ENUM('low', 'medium', 'high', 'critical') DEFAULT 'medium' COMMENT 'Nivel de gravedad',
    title VARCHAR(255) NOT NULL COMMENT 'Título de la alerta',
    description TEXT COMMENT 'Descripción detallada',
    target_id VARCHAR(100) COMMENT 'ID de la entidad relacionada',
    target_type VARCHAR(50) COMMENT 'Tipo de entidad relacionada',
    resolved BOOLEAN DEFAULT FALSE COMMENT 'Indica si la alerta fue resuelta',
    resolved_by CHAR(36) COMMENT 'ID del administrador que resolvió',
    resolved_at DATETIME COMMENT 'Fecha de resolución',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    
    INDEX idx_severity (severity),
    INDEX idx_resolved (resolved)
);

-- 8. REPORTES PROGRAMADOS
CREATE TABLE scheduled_report (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'Identificador único',
    name VARCHAR(150) NOT NULL COMMENT 'Nombre del reporte',
    type VARCHAR(50) NOT NULL COMMENT 'Tipo de reporte (participants, attendance, disabilities)',
    schedule_cron VARCHAR(50) NOT NULL COMMENT 'Expresión CRON para programación',
    parameters JSON COMMENT 'Filtros y parámetros del reporte',
    recipients JSON COMMENT 'Lista de emails destino',
    last_run DATETIME COMMENT 'Fecha de última ejecución',
    next_run DATETIME COMMENT 'Fecha de próxima ejecución',
    is_active BOOLEAN DEFAULT TRUE COMMENT 'Indica si el reporte está activo',
    created_by CHAR(36) COMMENT 'ID del administrador que creó',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Reportes programados para generación automática';

-- 9. LOG DE ACCIONES DE IA
CREATE TABLE ai_action_log (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'Identificador único',
    user_id CHAR(36) NOT NULL COMMENT 'ID del usuario afectado (referencia a Users MS)',
    ai_feature VARCHAR(100) NOT NULL COMMENT 'Funcionalidad IA que generó la acción',
    prediction_value JSON COMMENT 'Valor o resultado de la predicción',
    confidence DECIMAL(3,2) COMMENT 'Nivel de confianza de la predicción (0-1)',
    recommended_action TEXT COMMENT 'Acción recomendada por la IA',
    was_applied BOOLEAN DEFAULT FALSE COMMENT 'Indica si la recomendación fue aplicada',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha del registro',
    
    INDEX idx_user_id (user_id),
    INDEX idx_ai_feature (ai_feature)
);

-- 10. BLOQUEOS DE USUARIOS
CREATE TABLE user_blocks (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()) COMMENT 'Identificador único del bloqueo',
    user_id CHAR(36) NOT NULL COMMENT 'ID del usuario bloqueado (referencia a Users MS)',
    blocked_by CHAR(36) NOT NULL COMMENT 'ID del administrador que bloqueó',
    reason TEXT COMMENT 'Motivo del bloqueo',
    block_type ENUM('temporal', 'permanente') NOT NULL COMMENT 'temporal = con expiración, permanente = sin expiración',
    expires_at DATETIME COMMENT 'Fecha de expiración (NULL si es permanente)',
    is_active BOOLEAN DEFAULT TRUE COMMENT 'TRUE = bloqueo vigente, FALSE = ya fue desbloqueado',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha del bloqueo',
    unblocked_at DATETIME COMMENT 'Fecha en que se levantó el bloqueo',
    
    INDEX idx_user_id (user_id),
    INDEX idx_is_active (is_active),
    INDEX idx_expires_at (expires_at)
);

-- 11. PERMISOS DEL SISTEMA
CREATE TABLE permission (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único del permiso',
    name VARCHAR(100) UNIQUE NOT NULL COMMENT 'Nombre del permiso (users.view, users.block, events.create)',
    resource VARCHAR(50) NOT NULL COMMENT 'Recurso afectado (users, events, sports, reports, ai)',
    action VARCHAR(50) NOT NULL COMMENT 'Acción permitida (create, read, update, delete, approve)',
    description TEXT COMMENT 'Descripción del permiso',
    
    INDEX idx_resource_action (resource, action)
);
-- 12. RELACIÓN ROL-PERMISO
CREATE TABLE role_permission (
    role_id INT NOT NULL COMMENT 'ID del rol',
    permission_id INT NOT NULL COMMENT 'ID del permiso',
    assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de asignación',
    assigned_by CHAR(36) COMMENT 'ID del administrador que asignó',
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES admin_role(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
);

-- 13. PARÁMETROS GLOBALES DEL SISTEMA
CREATE TABLE system_parameters (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único',
    param_key VARCHAR(100) UNIQUE NOT NULL COMMENT 'Clave del parámetro',
    param_value VARCHAR(255) NOT NULL COMMENT 'Valor del parámetro',
    param_type ENUM('integer', 'boolean', 'string') DEFAULT 'string' COMMENT 'Tipo de dato del valor',
    description TEXT COMMENT 'Descripción del parámetro',
    updated_by CHAR(36) COMMENT 'ID del administrador que actualizó',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de actualización',
    
    INDEX idx_param_key (param_key)
);