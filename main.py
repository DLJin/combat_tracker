import os.path
import yaml
import io

from entity import Entity
from combat_tracker import CombatTracker

def fetch_encounter(file: str):
    print("Ingesting encounter: ", file)
    
    # Read YAML file
    with open("sources/encounters/" + file + ".yaml", 'r') as encounter_file:
        enemies = yaml.safe_load(encounter_file)

    entity_list = []

    # Extract every enemy type in the encounter
    for enemy_type in enemies:
        enemy_type_list = str(enemies[enemy_type]).split(",")
        #print(enemy_type)
        #print(enemy_type_list)

        # Get number of enemies, and what modifiers need to be applied
        num_of_enemy_type = 0
        for i in range(0, len(enemy_type_list)):
            if (i == 0):
                num_of_enemy_type = int(enemy_type_list[i])
            if (i == 1):
                print("Cannot handle enemy modifiers right now")

        # Create correct number of enemies of this type
        for enemy_num in range(0, num_of_enemy_type):
            # Get values for base enemy
            with open("sources/enemies/" + enemy_type + ".yaml", 'r') as enemy_file:
                enemy_data = yaml.safe_load(enemy_file)

            # Create components of each enemy
            enemy_components = {}
            for component in enemy_data:
                enemy_components[component] = enemy_data[component]
            #print(enemy_components)
            entity = Entity(enemy_type + "_" + str(enemy_num + 1), enemy_components)
            entity_list.append(entity)

    return entity_list

def main():
    #file_name = input("Enter encounter file: ")

    ct = CombatTracker(fetch_encounter("goblins"))

if __name__ == "__main__":
    main()