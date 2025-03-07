--- Function for Postgres to get the "Previous Set" for a Workout.
--- It works by using a list of 'exercise_ids' as input and uses the current authenticated user to ensure only their data is fetched
--- It then JOINS the required tables and creates a partition using the 'exercise_id' and 'sort_order'
--- This will then return all the latest sets for a specific exercise, for all setIndices (sort_order) that exist

CREATE OR REPLACE FUNCTION get_user_latest_sets_for_exercises(
    exercise_ids uuid[]  -- Input: List of exercise_ids
)
RETURNS TABLE(
    id uuid,
    exercise_id uuid,
    reps bigint,
    weight double precision,
    "time" bigint,
    sort_order bigint,
    created_at timestamp with time zone
) AS $$
WITH RankedSets AS (
    SELECT
        whes.id,
        whe.exercise_id,  -- Using the one from "WorkoutHistoryExercise"
        whes.reps,
        whes.weight,
        whes.time,
        whes.sort_order,
        whes.created_at,
        ROW_NUMBER() OVER (PARTITION BY whe.exercise_id, whes.sort_order ORDER BY whes.created_at DESC) AS rn -- Partition to get a combined "hash key" using exercise_id and sort_order
    FROM "public"."WorkoutHistoryExerciseSet" whes
    JOIN "public"."WorkoutHistoryExercise" whe ON whe.id = whes.workout_history_exercise_id
    JOIN "public"."WorkoutHistory" wh ON wh.id = whe.workout_history_id
    WHERE whe.exercise_id = ANY(exercise_ids)
      AND wh.user_id = auth.uid() -- only get sets that are linked to a history that is owned by the authenticated user
)
SELECT id, exercise_id, reps, weight, time, sort_order, created_at
FROM RankedSets
WHERE rn = 1;
$$ LANGUAGE sql STABLE;