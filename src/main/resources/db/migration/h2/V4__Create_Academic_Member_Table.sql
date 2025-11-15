-- ============================================================================
-- Table: academic_member
-- Description: Stores academic members (students, instructors, admins)
-- Primary Key: academic_member_id
-- Foreign Keys: academic_year_id, role_id, university_id
-- Relationships: N:1 with academic_year, N:1 with roles, N:1 with university
--               1:N with attendance, 1:N with notification, 1:N with enrollment
--               1:N with course_instructor, 1:N with lecture
-- Database: H2 (R2DBC Compatible)
-- ============================================================================

CREATE TABLE IF NOT EXISTS academic_member (
    -- Primary Key
    academic_member_id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

    -- Personal Information
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birthdate DATE NOT NULL,

    -- Authentication
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,

    -- Contact Information
    email VARCHAR(255) NOT NULL UNIQUE,
    email_verified BOOLEAN DEFAULT FALSE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    device_id VARCHAR(255) NOT NULL,

    -- University Information
    university_number VARCHAR(50) NOT NULL UNIQUE,

    -- Status
    soft_delete BOOLEAN DEFAULT FALSE NOT NULL,

    -- Foreign Keys
    academic_year_id UUID NOT NULL,
    role_id UUID NOT NULL,
    university_id UUID NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    -- Constraints
    CONSTRAINT fk_academic_member_academic_year FOREIGN KEY (academic_year_id)
        REFERENCES academic_year(academic_year_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_academic_member_role FOREIGN KEY (role_id)
        REFERENCES roles(role_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_academic_member_university FOREIGN KEY (university_id)
        REFERENCES university(universities_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_academic_member_username ON academic_member(username);
CREATE INDEX IF NOT EXISTS idx_academic_member_device_id ON academic_member(device_id);
CREATE INDEX IF NOT EXISTS idx_academic_member_email ON academic_member(email);
CREATE INDEX IF NOT EXISTS idx_academic_member_university_number ON academic_member(university_number);
CREATE INDEX IF NOT EXISTS idx_academic_member_academic_year_id ON academic_member(academic_year_id);
CREATE INDEX IF NOT EXISTS idx_academic_member_role_id ON academic_member(role_id);
CREATE INDEX IF NOT EXISTS idx_academic_member_university_id ON academic_member(university_id);
CREATE INDEX IF NOT EXISTS idx_academic_member_soft_delete ON academic_member(soft_delete);
CREATE INDEX IF NOT EXISTS idx_academic_member_email_verified ON academic_member(email_verified);

-- Comments
COMMENT ON TABLE academic_member IS 'Stores academic members information (students, instructors, admins)';
COMMENT ON COLUMN academic_member.academic_member_id IS 'Unique identifier for academic member';
COMMENT ON COLUMN academic_member.first_name IS 'First name';
COMMENT ON COLUMN academic_member.last_name IS 'Last name';
COMMENT ON COLUMN academic_member.username IS 'Unique username for login';
COMMENT ON COLUMN academic_member.password_hash IS 'Hashed password';
COMMENT ON COLUMN academic_member.birthdate IS 'Date of birth';
COMMENT ON COLUMN academic_member.university_number IS 'University identification number';
COMMENT ON COLUMN academic_member.email IS 'Email address';
COMMENT ON COLUMN academic_member.email_verified IS 'Email verification status';
COMMENT ON COLUMN academic_member.phone IS 'Phone number';
COMMENT ON COLUMN academic_member.soft_delete IS 'Soft delete flag';
COMMENT ON COLUMN academic_member.academic_year_id IS 'Reference to academic year';
COMMENT ON COLUMN academic_member.role_id IS 'Reference to role';
COMMENT ON COLUMN academic_member.university_id IS 'Reference to university';
COMMENT ON COLUMN academic_member.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN academic_member.updated_at IS 'Timestamp when record was last updated';