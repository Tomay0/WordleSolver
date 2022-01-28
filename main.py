from re import L

def map_counter(map, key):
    if key not in map:
        map[key] = 0

    map[key] += 1

def map_array(map, key, value):
    if key not in map:
        map[key] = []

    map[key].append(value)

class Word:
    def __init__(self, word):
        self.word = word
        self.letter_hash = {}

        for c in word:
            map_counter(self.letter_hash, c)

    def __repr__(self):
        return self.word.__repr__

    def __str__(self):
        return self.word

    def count_chars(self, c):
        if c not in self.letter_hash:
            return 0

        return self.letter_hash[c]

    def char_at(self, i, c):
        return self.word[i] == c


class GuessLogic:
    def __init__(self, correct, wrong_places, exact_letter_counts, display_array):
        self.correct = correct
        self.wrong_places = wrong_places
        self.exact_letter_counts = exact_letter_counts
        self.display_array = display_array

    def is_possible(self, word):

        # check correct place
        for i, c in self.correct.items():
            if word.char_at(i, c):
                return False

        # check wrong place
        for c, places in self.wrong_places.items():
            for i in places:
                if word.char_at(i, c):
                    return False

            if word.count_chars(c) < len(places):
                return False

        # check exact counts
        for c, v in self.exact_letter_counts.items():
            if word.count_chars(c) != v:
                return False

        return True

    def __str__(self):
        return f"Correct: {self.correct}\nWrong Places: {self.wrong_places}\nExact Counts: {self.exact_letter_counts}\nDisplay Array: {self.display_array}"



def guess_state(guess, solution):
    # correct places
    correct = {}
    letter_counts = {}
    for i, (g, s) in enumerate(zip(guess.word, solution.word)):
        if g == s:
            correct[i] = g

            map_counter(letter_counts, g)

    letter_counts2 = {}
    wrong_places = {}
    exact_letter_counts = {}

    display_array = []

    # incorrect places / wrong positions
    for i, g in enumerate(guess.word):
        map_counter(letter_counts2, g)

        solution_count = solution.count_chars(g)

        if i not in correct:
            map_counter(letter_counts, g)
            if solution_count < letter_counts[g]:
                # Not the right place
                display_array.append(0)

                exact_letter_counts[g] = solution_count
            else:
                map_array(wrong_places, g, i)
                display_array.append(1)

        else:
            display_array.append(2)

    return GuessLogic(correct, wrong_places, exact_letter_counts, display_array)


def possible_words(guess_logic, words):
    words_return = set()

    for word in words:
        if guess_logic.is_possible(word):
            words_return.add(word)

    return words_return


allowed_guesses = []
possible_solutions = []

with open('valid_words.txt') as f:
    allowed_guesses = [Word(x.strip()) for x in f.readlines()]

with open('all_solutions.txt') as f:
    possible_solutions = [Word(x.strip()) for x in f.readlines()]

# Correct: {2: 'c'}
# Wrong Place: {'c': [3]}
# Exact Counts: {'m': 0, 'e': 0, 'a': 0}
# Display Array: [0, 0, 2, 1, 0]
# Correct: {1: 'o', 4: 's'}
# Wrong Place: {}
# Exact Counts: {'f': 0, 'o': 1, 'd': 0}
# Display Array: [0, 2, 0, 0, 2]
# Correct: {2: 'c', 3: 'k', 4: 's'}
# Wrong Place: {}
# Exact Counts: {'k': 1, 'i': 0}
# Display Array: [0, 0, 2, 2, 2]
# Correct: {}
# Wrong Place: {'s': [0], 'c': [1], 'o': [2]}
# Exact Counts: {'r': 0, 'e': 0}
# Display Array: [1, 1, 1, 0, 0]
# Correct: {}
# Wrong Place: {'c': [1, 3], 'o': [2], 'k': [4]}
# Exact Counts: {'a': 0}
# Display Array: [0, 1, 1, 1, 1]
# print(guess_state(Word("mecca"), Word("cocks")))
# print(guess_state(Word("foods"), Word("cocks")))
# print(guess_state(Word("kicks"), Word("cocks")))
# print(guess_state(Word("score"), Word("cocks")))
# print(guess_state(Word("acock"), Word("cocks")))


def all_starting_word_scores():
    # what should the first guess be ?
    s = {}

    for i, starting_word in enumerate(allowed_guesses):
        total_solutions = 0
        for solution in possible_solutions:
            state = guess_state(starting_word, solution)
            words = possible_words(state, possible_solutions)
            total_solutions += len(words)

        s[starting_word.word] = total_solutions / len(possible_solutions)

        print(f"{starting_word}: average words: {s[starting_word.word]}")

    words_sorted = sorted(s.keys(), lambda x: -s[x])

    print("Best:", words_sorted[0])
    print("Worst:", words_sorted[-1])

def best_starting_word():
    best_word = ""

    bad = len(possible_solutions) ** 2
    best_count = bad


    for starting_word in allowed_guesses:
        total_solutions = 0
        for solution in possible_solutions:
            c = len(possible_words(guess_state(starting_word, solution), possible_solutions))

            total_solutions += c

            if total_solutions >= best_count:
                total_solutions = bad
                break

        if total_solutions < best_count:
            print(f"Best word: {starting_word.word}")
            print(f"Count: {total_solutions / len(possible_solutions)}")
            best_count = total_solutions
            best_word = starting_word.word

    return best_word


print(best_starting_word())


# main()
