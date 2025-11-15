-- ============================================================================
-- Table: course
-- Description: Stores course information
-- Primary Key: course_id
-- Foreign Keys: university_id, academic_year_id
-- Relationships: N:1 with university, N:1 with academic_year
--               1:N with lecture, 1:N with enrollment
--               N:M with academic_member (through course_instructor)
-- ============================================================================

CREATE TABLE IF NOT EXISTS course (
    -- Primary Key
    course_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Attributes
    code VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    soft_delete BOOLEAN DEFAULT FALSE NOT NULL,

    -- Foreign Keys
    university_id UUID NOT NULL,
    academic_year_id UUID NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    -- Constraints
    CONSTRAINT fk_course_university FOREIGN KEY (university_id)
        REFERENCES university(universities_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_course_academic_year FOREIGN KEY (academic_year_id)
        REFERENCES academic_year(academic_year_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uq_course_code_university_year UNIQUE (code, university_id, academic_year_id)
);

-- Indexes
CREATE INDEX idx_course_code ON course(code);
CREATE INDEX idx_course_name ON course(name);
CREATE INDEX idx_course_university_id ON course(university_id);
CREATE INDEX idx_course_academic_year_id ON course(academic_year_id);
CREATE INDEX idx_course_soft_delete ON course(soft_delete) WHERE soft_delete = FALSE;

-- Comments
COMMENT ON TABLE course IS 'Stores course information';
COMMENT ON COLUMN course.course_id IS 'Unique identifier for course';
COMMENT ON COLUMN course.code IS 'Course code';
COMMENT ON COLUMN course.name IS 'Course name';
COMMENT ON COLUMN course.description IS 'Course description';
COMMENT ON COLUMN course.soft_delete IS 'Soft delete flag';
COMMENT ON COLUMN course.university_id IS 'Reference to university';
COMMENT ON COLUMN course.academic_year_id IS 'Reference to academic year';
COMMENT ON COLUMN course.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN course.updated_at IS 'Timestamp when record was last updated';