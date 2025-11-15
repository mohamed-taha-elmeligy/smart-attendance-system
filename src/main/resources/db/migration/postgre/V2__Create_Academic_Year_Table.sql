-- ============================================================================
-- Table: academic_year
-- Description: Stores academic year information
-- Primary Key: academic_year_id
-- Foreign Keys: None
-- Relationships: 1:N with academic_member, 1:N with course
-- ============================================================================

CREATE TABLE IF NOT EXISTS academic_year (
    -- Primary Key
    academic_year_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Attributes
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(500) NOT NULL,
    soft_delete BOOLEAN DEFAULT FALSE NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Indexes
CREATE INDEX idx_academic_year_code ON academic_year(code);
CREATE INDEX idx_academic_year_soft_delete ON academic_year(soft_delete) WHERE soft_delete = FALSE;

-- Comments
COMMENT ON TABLE academic_year IS 'Stores academic year information';
COMMENT ON COLUMN academic_year.academic_year_id IS 'Unique identifier for academic year';
COMMENT ON COLUMN academic_year.code IS 'Academic year code (e.g., 2024-2025)';
COMMENT ON COLUMN academic_year.description IS 'Academic year description';
COMMENT ON COLUMN academic_year.soft_delete IS 'Soft delete flag';
COMMENT ON COLUMN academic_year.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN academic_year.updated_at IS 'Timestamp when record was last updated';