-- ============================================================================
-- Table: notification
-- Description: Stores notifications for academic members
-- Primary Key: notification_id
-- Foreign Keys: academic_member_id
-- Relationships: N:1 with academic_member
-- Database: H2 (R2DBC Compatible)
-- Note: H2 doesn't support ENUM, using VARCHAR with CHECK constraint
-- ============================================================================

CREATE TABLE IF NOT EXISTS notification (
    -- Primary Key
    notification_id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,

    -- Notification Details
    message CLOB NOT NULL,
    type VARCHAR(20) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE NOT NULL,

    -- Foreign Keys
    academic_member_id UUID NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    -- Constraints
    CONSTRAINT fk_notification_academic_member FOREIGN KEY (academic_member_id)
        REFERENCES academic_member(academic_member_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_notification_type CHECK (type IN ('ALERT', 'WARNING', 'INFO', 'REMINDER'))
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_notification_academic_member_id ON notification(academic_member_id);
CREATE INDEX IF NOT EXISTS idx_notification_type ON notification(type);
CREATE INDEX IF NOT EXISTS idx_notification_is_read ON notification(is_read);
CREATE INDEX IF NOT EXISTS idx_notification_created_at ON notification(created_at);

-- Comments
COMMENT ON TABLE notification IS 'Stores notifications for academic members';
COMMENT ON COLUMN notification.notification_id IS 'Unique identifier for notification';
COMMENT ON COLUMN notification.message IS 'Notification message content';
COMMENT ON COLUMN notification.type IS 'Notification type (ALERT, WARNING, INFO, REMINDER)';
COMMENT ON COLUMN notification.is_read IS 'Flag indicating if notification has been read';
COMMENT ON COLUMN notification.academic_member_id IS 'Reference to academic member';
COMMENT ON COLUMN notification.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN notification.updated_at IS 'Timestamp when record was last updated';