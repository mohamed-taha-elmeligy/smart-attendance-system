-- ============================================================================
-- Table: lecture
-- Description: Stores lecture sessions
-- Primary Key: lecture_id
-- Foreign Keys: course_id, instructor_academic_member_id
-- Relationships: N:1 with course, N:1 with academic_member (instructor)
--               1:N with attendance, 1:N with qr_code
-- ============================================================================

CREATE TABLE IF NOT EXISTS lecture (
    -- Primary Key
    lecture_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Lecture Details
    lecture_date DATE NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    room VARCHAR(50) NOT NULL,

    -- Status
    status BOOLEAN DEFAULT TRUE NOT NULL,
    soft_delete BOOLEAN DEFAULT FALSE NOT NULL,

    -- Foreign Keys
    course_id UUID NOT NULL,
    instructor_academic_member_id UUID NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    -- Constraints
    CONSTRAINT fk_lecture_course FOREIGN KEY (course_id)
        REFERENCES course(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_lecture_instructor FOREIGN KEY (instructor_academic_member_id)
        REFERENCES academic_member(academic_member_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_lecture_time CHECK (end_time > start_time),
    CONSTRAINT chk_day_of_week CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'))
);

-- Indexes
CREATE INDEX idx_lecture_course_id ON lecture(course_id);
CREATE INDEX idx_lecture_instructor_id ON lecture(instructor_academic_member_id);
CREATE INDEX idx_lecture_date ON lecture(lecture_date);
CREATE INDEX idx_lecture_day_of_week ON lecture(day_of_week);
CREATE INDEX idx_lecture_status ON lecture(status) WHERE status = TRUE;
CREATE INDEX idx_lecture_soft_delete ON lecture(soft_delete) WHERE soft_delete = FALSE;
CREATE INDEX idx_lecture_room ON lecture(room);

-- Comments
COMMENT ON TABLE lecture IS 'Stores lecture sessions';
COMMENT ON COLUMN lecture.lecture_id IS 'Unique identifier for lecture';
COMMENT ON COLUMN lecture.lecture_date IS 'Date of the lecture';
COMMENT ON COLUMN lecture.day_of_week IS 'Day of the week (MONDAY, TUESDAY, etc.)';
COMMENT ON COLUMN lecture.start_time IS 'Lecture start time';
COMMENT ON COLUMN lecture.end_time IS 'Lecture end time';
COMMENT ON COLUMN lecture.room IS 'Room number or location';
COMMENT ON COLUMN lecture.status IS 'Lecture status (active/inactive)';
COMMENT ON COLUMN lecture.soft_delete IS 'Soft delete flag';
COMMENT ON COLUMN lecture.course_id IS 'Reference to course';
COMMENT ON COLUMN lecture.instructor_academic_member_id IS 'Reference to instructor';
COMMENT ON COLUMN lecture.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN lecture.updated_at IS 'Timestamp when record was last updated';