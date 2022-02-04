import json
import os

def parse(dir):
  branch = {}
  with open(dir + "/guess.yml") as file:
    lines = file.readlines()

    branch["guess"] = lines[0].strip().split(": ")[1]

    for i in range(2, len(lines)):
      split = lines[i].strip().split(": ")
      branch[split[0]] = split[1]

  for file in os.listdir(dir):
    if str(file) != "guess.yml":
      branch[str(file)] = parse(f"{dir}/{str(file)}")

  return branch

def main():
  # TODO make this automatically save to JSON in the java code
  tree = parse("../tree")

  with open("../wordle-solver-app/src/tree.json", "w") as f:
    json.dump(tree, f)

main()