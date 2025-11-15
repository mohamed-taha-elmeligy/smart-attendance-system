-- ============================================================================
-- Table: course_instructor
-- Description: Junction table for many-to-many relationship between courses and instructors
-- Primary Key: course_instructor_id
-- Foreign Keys: instructor_academic_member_id, course_id
-- Relationships: N:M relationship between academic_member and course
-- ============================================================================

CREATE TABLE IF NOT EXISTS course_instructor (
    -- Primary Key
    course_instructor_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Foreign Keys
    instructor_academic_member_id UUID NOT NULL,
    course_id UUID NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    -- Constraints
    CONSTRAINT fk_course_instructor_academic_member FOREIGN KEY (instructor_academic_member_id)
        REFERENCES academic_member(academic_member_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_course_instructor_course FOREIGN KEY (course_id)
        REFERENCES course(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_instructor_course UNIQUE (instructor_academic_member_id, course_id)
);

-- Indexes
CREATE INDEX idx_course_instructor_academic_member_id ON course_instructor(instructor_academic_member_id);
CREATE INDEX idx_course_instructor_course_id ON course_instructor(course_id);

-- Comments
COMMENT ON TABLE course_instructor IS 'Junction table linking instructors to courses';
COMMENT ON COLUMN course_instructor.course_instructor_id IS 'Unique identifier for course instructor relationship';
COMMENT ON COLUMN course_instructor.instructor_academic_member_id IS 'Reference to instructor (academic member)';
COMMENT ON COLUMN course_instructor.course_id IS 'Reference to course';
COMMENT ON COLUMN course_instructor.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN course_instructor.updated_at IS 'Timestamp when record was last updated';