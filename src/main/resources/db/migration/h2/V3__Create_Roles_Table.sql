-- ============================================================================
-- Table: roles
-- Description: Stores user roles (Student, Instructor, Admin, etc.)
-- Primary Key: role_id
-- Foreign Keys: None
-- Relationships: 1:N with academic_member
-- Database: H2 (R2DBC Compatible)
-- ============================================================================

CREATE TABLE IF NOT EXISTS roles (
    -- Primary Key
    role_id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

    -- Attributes
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    soft_delete BOOLEAN DEFAULT FALSE NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_roles_name ON roles(name);
CREATE INDEX IF NOT EXISTS idx_roles_soft_delete ON roles(soft_delete);

-- Comments
COMMENT ON TABLE roles IS 'Stores user roles information';
COMMENT ON COLUMN roles.role_id IS 'Unique identifier for role';
COMMENT ON COLUMN roles.name IS 'Role name (e.g., STUDENT, INSTRUCTOR, ADMIN)';
COMMENT ON COLUMN roles.description IS 'Role description';
COMMENT ON COLUMN roles.soft_delete IS 'Soft delete flag';
COMMENT ON COLUMN roles.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN roles.updated_at IS 'Timestamp when record was last updated';