import os.path
import yaml
import io
from typing import List
from tabulate import tabulate

from entity import Entity

class CombatTracker:
    
    def __init__(self, entity_list: List[Entity]) -> None:
        print("CombatTracker init")