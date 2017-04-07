#!/usr/bin/python
'''
This is a script for finding Rhymes in Bulgarian.
'''

import sys
import re
import os

def generate_words_database():
    affixes = parse_affixes_file()
    with open("bg_BG_utf8.dic") as source_dict:
        with open("bg_BG.data", 'w+') as final_dict:
            r = re.compile(r'^(?P<word>\S+)/(?P<affix>\w)$')
            for line in source_dict:
                m = r.search(line)
                if m:
                    word, affix = m.group('word', 'affix')
                    rules = affixes[affix]
                    for rule in rules:
                        if rule['remove'] == '0':
                            final_dict.write(word + rule['ending'] + '\n')
                        elif rule['remove'] == word[-len(rule['remove']):]:
                                final_dict.write(word[:-len(rule['remove'])] +
                                                 (rule['ending'] if rule['ending'] != '0' else '') +
                                                 '\n')
                elif '/' not in line:
                    # The line does not contain a '/' and so has no special affixes.
                    final_dict.write(line)


def parse_affixes_file() :
    affixes = {}
    r = re.compile(r'^(?P<type>\b\w+\b)\s+(?P<name>\b\w+\b)\s+(?P<remove>\b\w+\b)\s+(?P<ending>\S+)\s+(?P<regex>\S+)\s*$')
    with open("bg_BG_utf8.aff") as f:
        for line in f:
            m = r.search(line)
            #print(line)
            #print(m)
            if not m:
                continue
            if m.group('name') not in affixes.keys():
                affixes[m.group('name')] = []
            affixes[m.group('name')].append(m.groupdict())
    return affixes

def load_dict_into_ram():
    with open("bg_BG.data", 'r') as f:
        return {w.strip() for w in list(f)}

def return_n_rhymes(dic, search_string, n):
    assert len(search_string) >= n, "Search string is too short"
    return sorted([x for x in dic if len(x) >= n and x[-n:] == search_string[-n:]])

if __name__ == '__main__':
    dic = load_dict_into_ram()
    if len(sys.argv) in (2, 3):
        if len(sys.argv) == 2:
            print(return_n_rhymes(dic, sys.argv[1], 2))
        else:
            print(return_n_rhymes(dic, sys.argv[1], int(sys.argv[2])))
    else:
        print("Error")
