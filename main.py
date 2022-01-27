def guess_state(guess, solution):
    pass


def possible_words(word, game_state, words):
    pass


allowed_guesses = []
possible_solutions = []

with open('valid_words.txt') as f:
    allowed_guesses = [x.strip() for x in f.readlines()]
with open('all_solutions.txt') as f:
    possible_solutions = [x.strip() for x in f.readlines()]

print(guess_state("mecca", "cocks"))  # 0, 0, 2, 1, 0
print(guess_state("foods", "cocks"))  # 0, 2, 0, 0, 2
print(guess_state("kicks", "cocks"))  # 0, 0, 2, 2, 2
print(guess_state("score", "cocks"))  # 1, 1, 1, 0, 0


def main():
    # what should the first guess be ?
    s = {}

    for starting_word in allowed_guesses:
        total_solutions = 0
        for solution in possible_solutions:
            state = guess_state(starting_word, solution)
            words = possible_words(starting_word, state, possible_solutions)
            total_solutions += len(words)

        s[starting_word] = total_solutions / len(possible_solutions)


main()
