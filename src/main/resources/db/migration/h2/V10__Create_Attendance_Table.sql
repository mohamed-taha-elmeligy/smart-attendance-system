-- ============================================================================
-- Table: attendance
-- Description: Stores student attendance records
-- Primary Key: attendance_id
-- Foreign Keys: lecture_id, qr_code_id, student_academic_member_id
-- Relationships: N:1 with lecture, N:1 with qr_code, N:1 with academic_member (student)
-- Database: H2 (R2DBC Compatible)
-- ============================================================================

CREATE TABLE IF NOT EXISTS attendance (
    -- Primary Key
    attendance_id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

    -- Attendance Details
    check_in_time TIMESTAMP WITH TIME ZONE NOT NULL,
    ip_address VARCHAR(50) NOT NULL,
    device_id VARCHAR(255) NOT NULL,

    -- Status
    is_present BOOLEAN DEFAULT FALSE NOT NULL,
    location_verified BOOLEAN DEFAULT FALSE NOT NULL,

    -- Foreign Keys
    lecture_id UUID NOT NULL,
    qr_code_id UUID NOT NULL,
    student_academic_member_id UUID NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    -- Constraints
    CONSTRAINT fk_attendance_lecture FOREIGN KEY (lecture_id)
        REFERENCES lecture(lecture_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_attendance_qr_code FOREIGN KEY (qr_code_id)
        REFERENCES qr_code(qr_code_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_attendance_student FOREIGN KEY (student_academic_member_id)
        REFERENCES academic_member(academic_member_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_student_lecture UNIQUE (student_academic_member_id, lecture_id)
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_attendance_lecture_id ON attendance(lecture_id);
CREATE INDEX IF NOT EXISTS idx_attendance_device_id ON academic_member(device_id);
CREATE INDEX IF NOT EXISTS idx_attendance_qr_code_id ON attendance(qr_code_id);
CREATE INDEX IF NOT EXISTS idx_attendance_student_id ON attendance(student_academic_member_id);
CREATE INDEX IF NOT EXISTS idx_attendance_check_in_time ON attendance(check_in_time);
CREATE INDEX IF NOT EXISTS idx_attendance_is_present ON attendance(is_present);
CREATE INDEX IF NOT EXISTS idx_attendance_location_verified ON attendance(location_verified);
CREATE INDEX IF NOT EXISTS idx_attendance_ip_address ON attendance(ip_address);

-- Comments
COMMENT ON TABLE attendance IS 'Stores student attendance records';
COMMENT ON COLUMN attendance.attendance_id IS 'Unique identifier for attendance record';
COMMENT ON COLUMN attendance.check_in_time IS 'Timestamp when student checked in';
COMMENT ON COLUMN attendance.ip_address IS 'IP address of device used for check-in';
COMMENT ON COLUMN attendance.device_id IS 'Device identifier used for check-in';
COMMENT ON COLUMN attendance.is_present IS 'Flag indicating if student is marked present';
COMMENT ON COLUMN attendance.location_verified IS 'Flag indicating if location was verified';
COMMENT ON COLUMN attendance.lecture_id IS 'Reference to lecture';
COMMENT ON COLUMN attendance.qr_code_id IS 'Reference to QR code used';
COMMENT ON COLUMN attendance.student_academic_member_id IS 'Reference to student';
COMMENT ON COLUMN attendance.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN attendance.updated_at IS 'Timestamp when record was last updated';