-- ============================================================================
-- Migration: Add instructor_academic_member_id to course table
-- Description: Adds instructor reference to existing course table
-- ============================================================================

-- Step 1: Add the new column as nullable first
ALTER TABLE course
ADD COLUMN instructor_academic_member_id UUID;

-- Step 2: Add the foreign key constraint
ALTER TABLE course
ADD CONSTRAINT fk_course_instructor
    FOREIGN KEY (instructor_academic_member_id)
    REFERENCES academic_member(academic_member_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- Step 3: Create index for performance
CREATE INDEX idx_course_instructor_id ON course(instructor_academic_member_id);

-- Step 4: Update existing records with a default instructor (optional)
-- If you have a default instructor, update like this:
-- UPDATE course SET instructor_academic_member_id = '<some-uuid>' WHERE instructor_academic_member_id IS NULL;

-- Step 5: Make the column NOT NULL (after populating existing data)
ALTER TABLE course
ALTER COLUMN instructor_academic_member_id SET NOT NULL;

-- Step 6: Update table comment
COMMENT ON COLUMN course.instructor_academic_member_id IS 'Reference to instructor (academic member)';