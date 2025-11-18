-- ============================================================================
-- Table: enrollment
-- Description: Stores student enrollments in courses
-- Primary Key: enrollment_id
-- Foreign Keys: course_id, student_academic_member
-- Relationships: N:1 with course, N:1 with academic_member (student)
-- Database: H2 (R2DBC Compatible)
-- ============================================================================

CREATE TABLE IF NOT EXISTS enrollment (
    -- Primary Key
    enrollment_id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

    -- Attributes
    soft_delete BOOLEAN DEFAULT FALSE NOT NULL,

    -- Foreign Keys
    course_id UUID NOT NULL,
    student_academic_member UUID NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    -- Constraints
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id)
        REFERENCES course(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_academic_member)
        REFERENCES academic_member(academic_member_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_student_course UNIQUE (student_academic_member, course_id)
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_enrollment_course_id ON enrollment(course_id);
CREATE INDEX IF NOT EXISTS idx_enrollment_student_academic_member ON enrollment(student_academic_member);
CREATE INDEX IF NOT EXISTS idx_enrollment_soft_delete ON enrollment(soft_delete);

-- Comments
COMMENT ON TABLE enrollment IS 'Stores student enrollments in courses';
COMMENT ON COLUMN enrollment.enrollment_id IS 'Unique identifier for enrollment';
COMMENT ON COLUMN enrollment.soft_delete IS 'Soft delete flag';
COMMENT ON COLUMN enrollment.course_id IS 'Reference to course';
COMMENT ON COLUMN enrollment.student_academic_member IS 'Reference to student (academic member)';
COMMENT ON COLUMN enrollment.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN enrollment.updated_at IS 'Timestamp when record was last updated';