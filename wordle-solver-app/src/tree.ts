import tree_json from './tree.json';

export type GuessJson = {
  "guess": string
  "guesses": {[id: string]: GuessJson | string}
}

function descendTree(json: any): GuessJson {
  const g:string = json["guess"];
  const guesses: {[id: string]: GuessJson | string} = {};

  for (const k in Object.keys(json)) {
    if (k != "guess") {
      if (json[k] instanceof String) {
        guesses[k] = json[k]
      } else {
        guesses[k] = descendTree(json[k]);
      }
    }
  }

  return {"guess": g, "guesses": guesses};
}

const tree: GuessJson = descendTree(tree_json);
export default tree;