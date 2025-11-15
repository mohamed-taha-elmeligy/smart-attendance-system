-- ============================================================================
-- Table: university
-- Description: Stores university information
-- Primary Key: universities_id
-- Foreign Keys: None
-- Relationships: 1:N with academic_member, 1:N with course
-- ============================================================================

CREATE TABLE IF NOT EXISTS university (
    -- Primary Key
    universities_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Attributes
    name VARCHAR(255) NOT NULL,
    location VARCHAR(500) NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Indexes
CREATE INDEX idx_university_name ON university(name);
CREATE INDEX idx_university_location ON university(location);

-- Comments
COMMENT ON TABLE university IS 'Stores university information';
COMMENT ON COLUMN university.universities_id IS 'Unique identifier for university';
COMMENT ON COLUMN university.name IS 'University name';
COMMENT ON COLUMN university.location IS 'University location';
COMMENT ON COLUMN university.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN university.updated_at IS 'Timestamp when record was last updated';