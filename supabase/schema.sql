

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;


CREATE EXTENSION IF NOT EXISTS "pgsodium";






COMMENT ON SCHEMA "public" IS 'standard public schema';



CREATE EXTENSION IF NOT EXISTS "pg_graphql" WITH SCHEMA "graphql";






CREATE EXTENSION IF NOT EXISTS "pg_stat_statements" WITH SCHEMA "extensions";






CREATE EXTENSION IF NOT EXISTS "pgcrypto" WITH SCHEMA "extensions";






CREATE EXTENSION IF NOT EXISTS "pgjwt" WITH SCHEMA "extensions";






CREATE EXTENSION IF NOT EXISTS "supabase_vault" WITH SCHEMA "vault";






CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA "extensions";





SET default_tablespace = '';

SET default_table_access_method = "heap";


CREATE TABLE IF NOT EXISTS "public"."Category" (
    "id" "uuid" DEFAULT "gen_random_uuid"() NOT NULL,
    "name" character varying NOT NULL
);


ALTER TABLE "public"."Category" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."Equipment" (
    "id" "uuid" DEFAULT "gen_random_uuid"() NOT NULL,
    "name" character varying NOT NULL
);


ALTER TABLE "public"."Equipment" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."Exercise" (
    "id" "uuid" DEFAULT "gen_random_uuid"() NOT NULL,
    "name" character varying NOT NULL,
    "image_url" character varying,
    "image_detail_url" character varying,
    "steps" character varying[],
    "equipment_id" "uuid",
    "category_id" "uuid",
    "type" character varying DEFAULT 'Weight'::character varying NOT NULL,
    "user_id" "uuid"
);


ALTER TABLE "public"."Exercise" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."Settings" (
    "id" "uuid" DEFAULT "gen_random_uuid"() NOT NULL,
    "rest" bigint DEFAULT '60'::bigint NOT NULL,
    "rest_enabled" boolean DEFAULT false NOT NULL,
    "rest_vibration_enabled" boolean DEFAULT true NOT NULL,
    "unit" character varying DEFAULT 'kg'::character varying NOT NULL,
    "user_id" "uuid" NOT NULL
);


ALTER TABLE "public"."Settings" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."Workout" (
    "id" "uuid" DEFAULT "gen_random_uuid"() NOT NULL,
    "created_at" timestamp with time zone DEFAULT "now"() NOT NULL,
    "name" character varying NOT NULL,
    "unit" character varying DEFAULT 'kg'::character varying,
    "user_id" "uuid" NOT NULL
);


ALTER TABLE "public"."Workout" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."WorkoutExercise" (
    "id" "uuid" DEFAULT "gen_random_uuid"() NOT NULL,
    "exercise_id" "uuid" NOT NULL,
    "workout_id" "uuid" NOT NULL,
    "notes" character varying,
    "sort_order" bigint NOT NULL
);


ALTER TABLE "public"."WorkoutExercise" OWNER TO "postgres";


COMMENT ON TABLE "public"."WorkoutExercise" IS 'Exercise in a specific Workout';



CREATE TABLE IF NOT EXISTS "public"."WorkoutExerciseSet" (
    "id" "uuid" DEFAULT "gen_random_uuid"() NOT NULL,
    "workout_exercise_id" "uuid" NOT NULL,
    "reps" bigint,
    "weight" double precision,
    "time" bigint,
    "sort_order" bigint NOT NULL
);


ALTER TABLE "public"."WorkoutExerciseSet" OWNER TO "postgres";


COMMENT ON TABLE "public"."WorkoutExerciseSet" IS 'Set for a specific WorkoutExercise';



CREATE TABLE IF NOT EXISTS "public"."WorkoutHistory" (
    "id" "uuid" DEFAULT "gen_random_uuid"() NOT NULL,
    "created_at" timestamp with time zone DEFAULT "now"() NOT NULL,
    "name" character varying NOT NULL,
    "unit" character varying DEFAULT 'kg'::character varying,
    "user_id" "uuid" NOT NULL
);


ALTER TABLE "public"."WorkoutHistory" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."WorkoutHistoryExercise" (
    "id" "uuid" DEFAULT "gen_random_uuid"() NOT NULL,
    "name" character varying NOT NULL,
    "image_url" character varying,
    "type" character varying DEFAULT 'Weight'::character varying NOT NULL,
    "notes" character varying,
    "sort_order" bigint NOT NULL,
    "exercise_id" "uuid",
    "workout_history_id" "uuid" NOT NULL,
    "category" character varying,
    "equipment" character varying
);


ALTER TABLE "public"."WorkoutHistoryExercise" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."WorkoutHistoryExerciseSet" (
    "id" "uuid" DEFAULT "gen_random_uuid"() NOT NULL,
    "workout_history_exercise_id" "uuid" NOT NULL,
    "reps" bigint,
    "weight" double precision,
    "time" bigint,
    "sort_order" bigint NOT NULL
);


ALTER TABLE "public"."WorkoutHistoryExerciseSet" OWNER TO "postgres";


ALTER TABLE ONLY "public"."Category"
    ADD CONSTRAINT "Category_name_key" UNIQUE ("name");



ALTER TABLE ONLY "public"."Category"
    ADD CONSTRAINT "Category_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."Equipment"
    ADD CONSTRAINT "Equipment_name_key" UNIQUE ("name");



ALTER TABLE ONLY "public"."Equipment"
    ADD CONSTRAINT "Equipment_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."Exercise"
    ADD CONSTRAINT "Exercise_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."Settings"
    ADD CONSTRAINT "Settings_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."WorkoutExerciseSet"
    ADD CONSTRAINT "WorkoutExerciseSet_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."WorkoutExercise"
    ADD CONSTRAINT "WorkoutExercise_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."WorkoutHistoryExerciseSet"
    ADD CONSTRAINT "WorkoutHistoryExerciseSet_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."WorkoutHistoryExercise"
    ADD CONSTRAINT "WorkoutHistoryExercise_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."WorkoutHistory"
    ADD CONSTRAINT "WorkoutHistory_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."Workout"
    ADD CONSTRAINT "Workout_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."Exercise"
    ADD CONSTRAINT "Exercise_category_id_fkey" FOREIGN KEY ("category_id") REFERENCES "public"."Category"("id") ON UPDATE CASCADE ON DELETE SET NULL;



ALTER TABLE ONLY "public"."Exercise"
    ADD CONSTRAINT "Exercise_equipment_id_fkey" FOREIGN KEY ("equipment_id") REFERENCES "public"."Equipment"("id") ON UPDATE CASCADE ON DELETE SET NULL;



ALTER TABLE ONLY "public"."Exercise"
    ADD CONSTRAINT "Exercise_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "auth"."users"("id") ON UPDATE CASCADE ON DELETE SET NULL;



ALTER TABLE ONLY "public"."Settings"
    ADD CONSTRAINT "Settings_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "auth"."users"("id") ON UPDATE CASCADE ON DELETE CASCADE;



ALTER TABLE ONLY "public"."WorkoutExerciseSet"
    ADD CONSTRAINT "WorkoutExerciseSet_workout_exercise_id_fkey" FOREIGN KEY ("workout_exercise_id") REFERENCES "public"."WorkoutExercise"("id") ON UPDATE CASCADE ON DELETE CASCADE;



ALTER TABLE ONLY "public"."WorkoutExercise"
    ADD CONSTRAINT "WorkoutExercise_exercise_id_fkey" FOREIGN KEY ("exercise_id") REFERENCES "public"."Exercise"("id") ON UPDATE CASCADE ON DELETE CASCADE;



ALTER TABLE ONLY "public"."WorkoutExercise"
    ADD CONSTRAINT "WorkoutExercise_workout_id_fkey" FOREIGN KEY ("workout_id") REFERENCES "public"."Workout"("id") ON UPDATE CASCADE ON DELETE CASCADE;



ALTER TABLE ONLY "public"."WorkoutHistoryExerciseSet"
    ADD CONSTRAINT "WorkoutHistoryExerciseSet_workout_history_exercise_id_fkey" FOREIGN KEY ("workout_history_exercise_id") REFERENCES "public"."WorkoutHistoryExercise"("id") ON UPDATE CASCADE ON DELETE CASCADE;



ALTER TABLE ONLY "public"."WorkoutHistoryExercise"
    ADD CONSTRAINT "WorkoutHistoryExercise_exercise_id_fkey" FOREIGN KEY ("exercise_id") REFERENCES "public"."Exercise"("id") ON UPDATE CASCADE ON DELETE SET NULL;



ALTER TABLE ONLY "public"."WorkoutHistoryExercise"
    ADD CONSTRAINT "WorkoutHistoryExercise_workout_history_id_fkey" FOREIGN KEY ("workout_history_id") REFERENCES "public"."WorkoutHistory"("id") ON UPDATE CASCADE ON DELETE CASCADE;



ALTER TABLE ONLY "public"."WorkoutHistory"
    ADD CONSTRAINT "WorkoutHistory_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "auth"."users"("id") ON UPDATE CASCADE ON DELETE CASCADE;



ALTER TABLE ONLY "public"."Workout"
    ADD CONSTRAINT "Workout_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "auth"."users"("id") ON UPDATE CASCADE ON DELETE CASCADE;



CREATE POLICY "ALL - Authenticated User" ON "public"."Exercise" USING (true) WITH CHECK ((( SELECT "auth"."uid"() AS "uid") = "user_id"));



CREATE POLICY "ALL - Authenticated User" ON "public"."Settings" USING (true) WITH CHECK ((( SELECT "auth"."uid"() AS "uid") = "user_id"));



CREATE POLICY "ALL - Authenticated User" ON "public"."Workout" USING (true) WITH CHECK ((( SELECT "auth"."uid"() AS "uid") = "user_id"));



CREATE POLICY "ALL - Authenticated User" ON "public"."WorkoutExercise" USING (true) WITH CHECK ((( SELECT "auth"."uid"() AS "uid") IN ( SELECT "Workout"."user_id"
   FROM "public"."Workout"
  WHERE ("Workout"."id" = "WorkoutExercise"."workout_id"))));



CREATE POLICY "ALL - Authenticated User" ON "public"."WorkoutExerciseSet" USING (true) WITH CHECK ((( SELECT "auth"."uid"() AS "uid") IN ( SELECT "Workout"."user_id"
   FROM "public"."Workout"
  WHERE ("Workout"."id" IN ( SELECT "WorkoutExercise"."workout_id"
           FROM "public"."WorkoutExercise"
          WHERE ("WorkoutExercise"."id" = "WorkoutExerciseSet"."workout_exercise_id"))))));



CREATE POLICY "ALL - Authenticated User" ON "public"."WorkoutHistory" USING (true) WITH CHECK ((( SELECT "auth"."uid"() AS "uid") = "user_id"));



CREATE POLICY "ALL - Authenticated User" ON "public"."WorkoutHistoryExercise" USING (true) WITH CHECK ((( SELECT "auth"."uid"() AS "uid") IN ( SELECT "WorkoutHistory"."user_id"
   FROM "public"."WorkoutHistory"
  WHERE ("WorkoutHistory"."id" = "WorkoutHistoryExercise"."workout_history_id"))));



CREATE POLICY "ALL - Authenticated User" ON "public"."WorkoutHistoryExerciseSet" USING (true) WITH CHECK ((( SELECT "auth"."uid"() AS "uid") IN ( SELECT "WorkoutHistory"."user_id"
   FROM "public"."WorkoutHistory"
  WHERE ("WorkoutHistory"."id" IN ( SELECT "WorkoutHistoryExercise"."workout_history_id"
           FROM "public"."WorkoutHistoryExercise"
          WHERE ("WorkoutHistoryExercise"."id" = "WorkoutHistoryExerciseSet"."workout_history_exercise_id"))))));



ALTER TABLE "public"."Category" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."Equipment" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."Exercise" ENABLE ROW LEVEL SECURITY;


CREATE POLICY "READ - All Users" ON "public"."Category" FOR SELECT USING (true);



CREATE POLICY "READ - All Users" ON "public"."Equipment" FOR SELECT USING (true);



CREATE POLICY "READ - Public Exercises" ON "public"."Exercise" FOR SELECT USING (("user_id" = NULL::"uuid"));



ALTER TABLE "public"."Settings" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."Workout" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."WorkoutExercise" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."WorkoutExerciseSet" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."WorkoutHistory" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."WorkoutHistoryExercise" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."WorkoutHistoryExerciseSet" ENABLE ROW LEVEL SECURITY;




ALTER PUBLICATION "supabase_realtime" OWNER TO "postgres";


GRANT USAGE ON SCHEMA "public" TO "postgres";
GRANT USAGE ON SCHEMA "public" TO "anon";
GRANT USAGE ON SCHEMA "public" TO "authenticated";
GRANT USAGE ON SCHEMA "public" TO "service_role";



































































































































































































GRANT ALL ON TABLE "public"."Category" TO "anon";
GRANT ALL ON TABLE "public"."Category" TO "authenticated";
GRANT ALL ON TABLE "public"."Category" TO "service_role";



GRANT ALL ON TABLE "public"."Equipment" TO "anon";
GRANT ALL ON TABLE "public"."Equipment" TO "authenticated";
GRANT ALL ON TABLE "public"."Equipment" TO "service_role";



GRANT ALL ON TABLE "public"."Exercise" TO "anon";
GRANT ALL ON TABLE "public"."Exercise" TO "authenticated";
GRANT ALL ON TABLE "public"."Exercise" TO "service_role";



GRANT ALL ON TABLE "public"."Settings" TO "anon";
GRANT ALL ON TABLE "public"."Settings" TO "authenticated";
GRANT ALL ON TABLE "public"."Settings" TO "service_role";



GRANT ALL ON TABLE "public"."Workout" TO "anon";
GRANT ALL ON TABLE "public"."Workout" TO "authenticated";
GRANT ALL ON TABLE "public"."Workout" TO "service_role";



GRANT ALL ON TABLE "public"."WorkoutExercise" TO "anon";
GRANT ALL ON TABLE "public"."WorkoutExercise" TO "authenticated";
GRANT ALL ON TABLE "public"."WorkoutExercise" TO "service_role";



GRANT ALL ON TABLE "public"."WorkoutExerciseSet" TO "anon";
GRANT ALL ON TABLE "public"."WorkoutExerciseSet" TO "authenticated";
GRANT ALL ON TABLE "public"."WorkoutExerciseSet" TO "service_role";



GRANT ALL ON TABLE "public"."WorkoutHistory" TO "anon";
GRANT ALL ON TABLE "public"."WorkoutHistory" TO "authenticated";
GRANT ALL ON TABLE "public"."WorkoutHistory" TO "service_role";



GRANT ALL ON TABLE "public"."WorkoutHistoryExercise" TO "anon";
GRANT ALL ON TABLE "public"."WorkoutHistoryExercise" TO "authenticated";
GRANT ALL ON TABLE "public"."WorkoutHistoryExercise" TO "service_role";



GRANT ALL ON TABLE "public"."WorkoutHistoryExerciseSet" TO "anon";
GRANT ALL ON TABLE "public"."WorkoutHistoryExerciseSet" TO "authenticated";
GRANT ALL ON TABLE "public"."WorkoutHistoryExerciseSet" TO "service_role";



ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES  TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES  TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES  TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES  TO "service_role";






ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS  TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS  TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS  TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS  TO "service_role";






ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES  TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES  TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES  TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES  TO "service_role";






























RESET ALL;
