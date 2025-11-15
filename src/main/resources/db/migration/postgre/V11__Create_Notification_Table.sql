-- ============================================================================
-- Table: notification
-- Description: Stores notifications for academic members
-- Primary Key: notification_id
-- Foreign Keys: academic_member_id
-- Relationships: N:1 with academic_member
-- ============================================================================

-- Create ENUM type for notification types
CREATE TYPE notification_type AS ENUM ('ALERT', 'WARNING', 'INFO', 'REMINDER');

CREATE TABLE IF NOT EXISTS notification (
    -- Primary Key
    notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Notification Details
    message TEXT NOT NULL,
    type notification_type NOT NULL,
    is_read BOOLEAN DEFAULT FALSE NOT NULL,

    -- Foreign Keys
    academic_member_id UUID NOT NULL,

    -- Audit Fields
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    -- Constraints
    CONSTRAINT fk_notification_academic_member FOREIGN KEY (academic_member_id)
        REFERENCES academic_member(academic_member_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Indexes
CREATE INDEX idx_notification_academic_member_id ON notification(academic_member_id);
CREATE INDEX idx_notification_type ON notification(type);
CREATE INDEX idx_notification_is_read ON notification(is_read) WHERE is_read = FALSE;
CREATE INDEX idx_notification_created_at ON notification(created_at DESC);

-- Comments
COMMENT ON TABLE notification IS 'Stores notifications for academic members';
COMMENT ON COLUMN notification.notification_id IS 'Unique identifier for notification';
COMMENT ON COLUMN notification.message IS 'Notification message content';
COMMENT ON COLUMN notification.type IS 'Notification type (ALERT, WARNING, INFO, REMINDER)';
COMMENT ON COLUMN notification.is_read IS 'Flag indicating if notification has been read';
COMMENT ON COLUMN notification.academic_member_id IS 'Reference to academic member';
COMMENT ON COLUMN notification.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN notification.updated_at IS 'Timestamp when record was last updated';