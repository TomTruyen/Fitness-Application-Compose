CREATE OR REPLACE FUNCTION get_user_latest_records_for_exercises (
  exercise_ids uuid[] -- Input: List of exercise_ids
) RETURNS TABLE (
  exercise_id uuid,
  highest_weight double precision,
  highest_time bigint,
  highest_volume double precision
) AS $$
WITH UserSets AS (
    SELECT
        whe.exercise_id,
        whes.reps,
        whes.weight,
        whes.time,
        whes.reps * whes.weight AS volume,
        whe.type AS exercise_type
    FROM "public"."WorkoutHistoryExerciseSet" whes
    JOIN "public"."WorkoutHistoryExercise" whe ON whe.id = whes.workout_history_exercise_id
    JOIN "public"."WorkoutHistory" wh ON wh.id = whe.workout_history_id
    WHERE whe.exercise_id = ANY(exercise_ids)
      AND wh.user_id = auth.uid() -- only get sets that are linked to a history that is owned by the authenticated user
),
FilteredSets AS (
    SELECT
        exercise_id,
        MAX(weight) FILTER (WHERE weight IS NOT NULL) AS highest_weight,
        MAX(time) FILTER (WHERE time IS NOT NULL) AS highest_time,
        MAX(volume) FILTER (WHERE volume IS NOT NULL) AS highest_volume
    FROM UserSets
    GROUP BY exercise_id
)
SELECT
    exercise_id,
    highest_weight,
    highest_time,
    highest_volume
FROM FilteredSets
WHERE (highest_weight IS NOT NULL OR highest_time IS NOT NULL OR highest_volume IS NOT NULL);
$$ LANGUAGE sql STABLE;