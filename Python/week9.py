# %%
import sys
import numpy as np

# %%
characters = np.array([' ']+list(open(sys.argv[1]).read())+[' '])
characters[~np.char.isalpha(characters)] = ' '
characters = np.char.upper(characters)
characters = np.char.replace(characters,'A','4')
characters = np.char.replace(characters,'E','3')
characters = np.char.replace(characters,'I','1')
characters = np.char.replace(characters,'O','0')
characters = np.char.replace(characters,'U','_')
# leet stuff here

# %%
stop_words = np.array(list(set(open('stop_words.txt').read().split(','))))
stop_words = np.char.upper(stop_words)
stop_words = np.char.replace(stop_words,'A','4')
stop_words = np.char.replace(stop_words,'E','3')
stop_words = np.char.replace(stop_words,'I','1')
stop_words = np.char.replace(stop_words,'O','0')
stop_words = np.char.replace(stop_words,'U','_')

# %%
spaces = characters == " "

# %%
# This returns a tuple for some reason
spaces_indices = np.where(spaces)[0]
spaces_indices = spaces_indices.repeat(2)[1:-1]

# %%
word_spans = np.reshape(spaces_indices, (-1, 2))

# %%
word_spans

# %%
span_lengths = (word_spans[:, 1] - word_spans[:, 0])
word_spans = word_spans[span_lengths > 2]

# %%
def get_word(span):
    chars = characters[span[0] : span[1]]
    return "".join(chars).strip()

words = np.array(list(map(get_word, word_spans)))

# %%
words

# %%
is_stop_word = np.isin(words, stop_words)

# %%
non_stop_words = words[~is_stop_word]

# %%
bigrams = np.reshape(non_stop_words.repeat(2)[1:-1], (-1, 2))

# %%
bigrams, counts = np.unique(bigrams, axis=0, return_counts=True)
wf_sorted = sorted(zip(bigrams, counts), key=lambda t: t[1], reverse=True)

# %%
wf_sorted[0]

# %%
for (a,b), count in wf_sorted[:5]:
    print(str(a) + ", " + str(b) + ": " + str(count))


