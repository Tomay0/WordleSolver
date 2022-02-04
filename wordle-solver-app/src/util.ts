import tree, {GuessJson} from './tree';

export type Failable<R> = {
  isError: true;
  error: string;
} | {
  isError: false;
  value: R;
}

export function getTreeBranch(dir: string): GuessJson | string | null {
  if (dir.length == 0) return tree;

  const split: Array<string> = dir.split("/")

  let t: GuessJson = tree;
  for (let i = 0; i < split.length - 1; i++) {
    if(split[i] in t["guesses"] && t["guesses"]) {
      const leaf: GuessJson | string = t["guesses"][split[i]];
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

export function createError(message: string): {isError: true, error: string} {
  return {isError: true, error: message};
}

export function createValue<R>(value: R): Failable<R> {
  return {isError: false, value}
}

export function descend(dir: string, guess: Array<number>): Failable<string> {
  // check parent directory
  const treeBranch: GuessJson | string | null = getTreeBranch(dir);

  if (treeBranch === null || typeof treeBranch === "string") {
    return createError("Invalid path")
  }

  const guessString: string = guess.join("");

  if (guessString in treeBranch["guesses"]) {
    const childBranch: GuessJson | string = treeBranch["guesses"][guessString];

    if (typeof childBranch === "string") {
      return createValue(childBranch);
    }

    return createValue(treeBranch["guess"]);
  }

  return createError("No valid solutions for this path");
}
