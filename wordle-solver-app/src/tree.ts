import { isMappedTypeNode } from 'typescript';
import { Failable, createError, createValue } from './util';

export type GuessTree = {
  "guess": string
  "guesses": {[id: string]: GuessTree | string}
}

export function createTree(json: any): GuessTree {
  const g:string = json["guess"];
  const guesses: {[id: string]: GuessTree | string} = {};

  for (const k of Object.keys(json)) {
    if (k !== "guess") {
      if (typeof json[k] === "string") {
        guesses[k] = json[k]
      } else {
        guesses[k] = createTree(json[k]);
      }
    }
  }

  return {"guess": g, "guesses": guesses};
}

function getTreeBranch(tree: GuessTree, dir: string): GuessTree | string | null {
  if (dir.length === 0) return tree;

  const split: Array<string> = dir.split("/")


  let t: GuessTree = tree;
  for (let i = 0; i < split.length - 1; i++) {
    if(split[i] in t["guesses"] && t["guesses"]) {
      const leaf: GuessTree | string = t["guesses"][split[i]];
      if (typeof leaf === "string") {
        return null;
      }

      t = leaf;
    } else {
      return null;
    }
  }

  const s: string = split[split.length - 1]

  if (s in t["guesses"]) {
    return t["guesses"][s];
  }

  return null;
}


export function treeDepths(tree: GuessTree, map:{[id: string]: number}, depth: number): {[id: string]: number} {
  for (const [logic, childTree] of Object.entries(tree["guesses"])) {
    if (typeof childTree === "string") {
      map[childTree] = depth;
      if (logic !== "22222") {
        map[childTree] += 1;
      }
    } else {
      treeDepths(childTree, map, depth + 1);
    }
  }
  return map;
}

export function evaluateTree(tree: GuessTree) {
  const depths: {[id: string]: number} = treeDepths(tree, {}, 1);
  const counts: {[id: number]: number} = {}

  for (const i of Object.values(depths)) {
    if (i in counts) {
      counts[i] += 1;
    } else {
      counts[i] = 1;
    }
  }
  return counts;
}

export function descendTree(tree: GuessTree, dir: string, guess: Array<number>): Failable<string> {
  // check parent directory
  const treeBranch: GuessTree | string | null = getTreeBranch(tree, dir);

  if (treeBranch === null || typeof treeBranch === "string") {
    return createError("Invalid path")
  }
  const guessString: string = guess.join("");

  if (guessString in treeBranch["guesses"]) {
    const childBranch: GuessTree | string = treeBranch["guesses"][guessString];

    if (typeof childBranch === "string") {
      return createValue(childBranch);
    }

    return createValue(childBranch["guess"]);
  }

  return createError("No valid solutions for this path");
}
