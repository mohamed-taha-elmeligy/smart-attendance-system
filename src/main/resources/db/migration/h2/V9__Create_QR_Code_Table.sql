-- ============================================================================
-- Table: qr_code
-- Description: Stores QR codes for attendance verification
-- Primary Key: qr_code_id
-- Foreign Keys: lecture_id
-- Relationships: N:1 with lecture, 1:N with attendance
-- Database: H2 (R2DBC Compatible)
-- ============================================================================

CREATE TABLE IF NOT EXISTS qr_code (
    -- Primary Key
    qr_code_id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

    -- QR Code Details
    uuid_token_hash VARCHAR(255) NOT NULL UNIQUE,
    network_info VARCHAR(500) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    duration_seconds BIGINT NOT NULL,

    -- Status
    activated BOOLEAN DEFAULT TRUE NOT NULL,
    expired BOOLEAN DEFAULT FALSE NOT NULL,

    -- Foreign Keys
    lecture_id UUID NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    -- Constraints
    CONSTRAINT fk_qr_code_lecture FOREIGN KEY (lecture_id)
        REFERENCES lecture(lecture_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_duration_seconds CHECK (duration_seconds > 0)
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_qr_code_lecture_id ON qr_code(lecture_id);
CREATE INDEX IF NOT EXISTS idx_qr_code_uuid_token_hash ON qr_code(uuid_token_hash);
CREATE INDEX IF NOT EXISTS idx_qr_code_expires_at ON qr_code(expires_at);
CREATE INDEX IF NOT EXISTS idx_qr_code_activated ON qr_code(activated);
CREATE INDEX IF NOT EXISTS idx_qr_code_expired ON qr_code(expired);

-- Comments
COMMENT ON TABLE qr_code IS 'Stores QR codes for attendance verification';
COMMENT ON COLUMN qr_code.qr_code_id IS 'Unique identifier for QR code';
COMMENT ON COLUMN qr_code.uuid_token_hash IS 'Hashed UUID token for QR code';
COMMENT ON COLUMN qr_code.network_info IS 'Network information for location verification';
COMMENT ON COLUMN qr_code.expires_at IS 'Expiration timestamp';
COMMENT ON COLUMN qr_code.duration_seconds IS 'Duration in seconds before expiration';
COMMENT ON COLUMN qr_code.activated IS 'QR code activation status';
COMMENT ON COLUMN qr_code.expired IS 'QR code expiration status';
COMMENT ON COLUMN qr_code.lecture_id IS 'Reference to lecture';
COMMENT ON COLUMN qr_code.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN qr_code.updated_at IS 'Timestamp when record was last updated';