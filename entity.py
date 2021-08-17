import random
import time
random.seed(time.time())

class Entity:
    _name = ""
    _health = 0
    _initiative = 0
    _statuses = []

    def __init__(self, name:str, components:dict) -> None:
        self._name = name
        for key in components:
            if key == "health":
                self._health = components[key]
            if key == "initiative":
                self._initiative = components[key] + random.randint(1,20)
            if key == "statuses":
                self._statuses = components[key]
        self.__print()

    def damage(self, amount:int, type:str):
        self._health = _health + amount

    def __print(self) -> None:
        print("=====================================")
        print("Name:\t\t", self._name)
        print("Health:\t\t", self._health)
        print("Initiative:\t", self._initiative)
        print("Statuses:\t", self._statuses)