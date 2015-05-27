ALTER TABLE qrtz_job_details ALTER COLUMN is_durable TYPE boolean USING (is_durable::boolean);
ALTER TABLE qrtz_job_details ALTER COLUMN is_nonconcurrent TYPE boolean USING (is_nonconcurrent::boolean);
ALTER TABLE qrtz_job_details ALTER COLUMN is_update_data TYPE boolean USING (is_update_data::boolean);
ALTER TABLE qrtz_job_details ALTER COLUMN requests_recovery TYPE boolean USING (requests_recovery::boolean);
ALTER TABLE qrtz_fired_triggers ALTER COLUMN requests_recovery TYPE boolean USING (requests_recovery::boolean);
