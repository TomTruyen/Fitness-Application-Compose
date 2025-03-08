const { createClient } = require('@supabase/supabase-js');
const fs = require('fs');

const SUPABASE_URL = '';
const SUPABASE_KEY = '';

const supabase = createClient(SUPABASE_URL, SUPABASE_KEY);

const jsonPath = './exercises.json';

const CATEGORY_TABLE = 'Category';
const EQUIPMENT_TABLE = 'Equipment';
const EXERCISE_TABLE = 'Exercise';

async function importSupabase() {
  const jsonFile = fs.readFileSync(jsonPath, 'utf8');
  const exercisesData = JSON.parse(jsonFile);

  // Categories
  const categories = exercisesData.map((exercise) => exercise.category);
  const uniqueCategories = [...new Set(categories)]
    .filter((category) => {
      return category !== '';
    })
    .map((category) => {
      return { name: category };
    });

  const categoryResult = await supabase
    .from(CATEGORY_TABLE)
    .insert(uniqueCategories)
    .select();

  console.log(
    'Inserted categories',
    categoryResult.data,
    'Error:',
    categoryResult.error
  );

  // Equipment
  const equipment = exercisesData.map((exercise) => exercise.equipment);
  const uniqueEquipment = [...new Set(equipment)]
    .filter((equipment) => {
      return equipment !== '';
    })
    .map((equipment) => {
      return { name: equipment };
    });

  const equipmentResult = await supabase
    .from(EQUIPMENT_TABLE)
    .insert(uniqueEquipment)
    .select();

  console.log(
    'Inserted equipment',
    equipmentResult.data,
    'Error:',
    equipmentResult.error
  );

  const categoryIdMapping = categoryResult.data;
  const equipmentIdMapping = equipmentResult.data;

  // Exercises
  const exercises = exercisesData.map((exercise) => {
    const newExercise = {
      ...exercise,
      category_id:
        categoryIdMapping.find(
          (category) => category.name === exercise.category
        )?.id ?? null,
      equipment_id:
        equipmentIdMapping.find(
          (equipment) => equipment.name === exercise.equipment
        )?.id ?? null,
      image_url: exercise.image,
      image_detail_url: exercise.image_detail,
      steps: exercise.description,
      type:
        exercise.type.charAt(0).toUpperCase() +
        exercise.type.slice(1).toLowerCase(),
    };

    delete newExercise.category;
    delete newExercise.equipment;
    delete newExercise.image;
    delete newExercise.image_detail;
    delete newExercise.description;

    return newExercise;
  });

  const { error } = await supabase.from(EXERCISE_TABLE).insert(exercises);

  console.log('Inserted exercises, error:', error);
}

importSupabase();
