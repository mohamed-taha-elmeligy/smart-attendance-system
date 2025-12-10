DROP INDEX IF EXISTS idx_attendance_device_id;

ALTER TABLE attendance
    ALTER COLUMN qr_code_id DROP NOT NULL,
    ALTER COLUMN ip_address DROP NOT NULL,
    ALTER COLUMN check_in_time DROP NOT NULL,

    DROP CONSTRAINT IF EXISTS uq_student_lecture,
    ADD CONSTRAINT uq_device_per_lecture UNIQUE (device_id, lecture_id);
