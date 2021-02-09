#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <iomanip>

const std::string FILE_NAME = "test";

typedef long long ll;

std::vector<std::pair<double, char>> realFrequency = {{0.08167, 'a'},
                                                      {0.01492, 'b'},
                                                      {0.02782, 'c'},
                                                      {0.04253, 'd'},
                                                      {0.12702, 'e'},
                                                      {0.02228, 'f'},
                                                      {0.02015, 'g'},
                                                      {0.06094, 'h'},
                                                      {0.06966, 'i'},
                                                      {0.00153, 'j'},
                                                      {0.00772, 'k'},
                                                      {0.04025, 'l'},
                                                      {0.02406, 'm'},
                                                      {0.06749, 'n'},
                                                      {0.07507, 'o'},
                                                      {0.01929, 'p'},
                                                      {0.00095, 'q'},
                                                      {0.05987, 'r'},
                                                      {0.06327, 's'},
                                                      {0.09056, 't'},
                                                      {0.02758, 'u'},
                                                      {0.00978, 'v'},
                                                      {0.0236,  'w'},
                                                      {0.0015,  'x'},
                                                      {0.01975, 'y'},
                                                      {0.00074, 'z'}};

auto comp = [](const std::pair<double, char> &a, const std::pair<double, char> &b) { return (a.first > b.first); };

std::size_t compareFrequency(std::vector<std::pair<double, char>> &a, std::vector<std::pair<double, char>> &b) {
    std::size_t qty = 0;
    for (size_t i = 0; i < 26; ++i)
        if (a[i].second != b[i].second)
            ++qty;
    return qty;
}

std::vector<ll>
count_letters_in_period(const std::string &text, const std::size_t &startLetter, const std::size_t &period) {
    std::vector<ll> lettersCounter(26, 0);
    for (size_t i = startLetter; i < text.size(); i += period)
        ++lettersCounter[text[i] - 'a'];
    return lettersCounter;
}

// possible word length = [maybeL, maybeR)
std::size_t find_key_length(const std::string &text, const std::size_t &maybeL, const std::size_t &maybeR) {
    std::vector<double> hits(maybeR - maybeL);
    for (std::size_t length = maybeL; length < maybeR; ++length) {
        std::vector<ll> lettersCounter = count_letters_in_period(text, 0, length);
        ll sum = 0;
        for (auto u : lettersCounter)
            sum += u * u;
        double n = (double) text.size() / length;
        hits[length - maybeL] = (double) sum / (n * n);
    }
    return std::max_element(hits.begin(), hits.end()) - hits.begin() + maybeL;
}

std::string find_key(const std::string &text, const std::size_t &keyLength) {
    std::string key(keyLength, 'a');
    for (std::size_t length = 0; length < keyLength; ++length) {
        std::vector<ll> lettersCounter = count_letters_in_period(text, length, keyLength);
        std::vector<double> frequency(26, 0);
        for (std::size_t letter = 0; letter < lettersCounter.size(); ++letter)
            frequency[letter] = (double) lettersCounter[letter] / text.size() * keyLength;

        std::vector<double> hits(26, 0);
        for (std::size_t letter = 0; letter < hits.size(); ++letter)
            for (size_t letterShift = 0; letterShift < 26; ++letterShift)
                hits[letter] += std::abs(frequency[(letter + letterShift) % 26] - realFrequency[letterShift].first);
        key[length] += std::min_element(hits.begin(), hits.end()) - hits.begin();
    }
    return key;
}

std::string decode(const std::string &text, const std::string &key) {
    std::string ans;
    for (std::size_t i = 0; i < text.size(); ++i)
        ans += (char) (text[i] - (int) (key[i % key.size()] - 'a') + (text[i] < key[i % key.size()] ? 26 : 0));
    return ans;
}

std::vector<std::pair<double, char>> get_sorted_frequency(const std::string &text) {
    std::vector<std::pair<double, char>> pairCurFrequency(26);
    std::vector<ll> lettersCounter(26, 0);
    std::vector<double> frequency(26);
    for (char u : text)
        ++lettersCounter[u - 'a'];
    for (size_t i = 0; i < lettersCounter.size(); ++i)
        frequency[i] = (double) lettersCounter[i] / text.size();
    for (size_t i = 0; i < lettersCounter.size(); ++i)
        pairCurFrequency[i] = {frequency[i], (char) ('a' + i)};
    sort(pairCurFrequency.begin(), pairCurFrequency.end(), comp);
    return pairCurFrequency;
}

void printCompare(std::vector<std::pair<double, char>> a, std::vector<std::pair<double, char>> &b) {
    for (size_t i = 0; i < 26; ++i)
        std::cout << std::setprecision(4)
                  << a[i].second << ' ' << a[i].first << "\t->\t" << b[i].second << ' ' << b[i].first << '\n';
}

signed main() {
    freopen((FILE_NAME + ".in").c_str(), "r", stdin);
    freopen((FILE_NAME + ".out").c_str(), "w", stdout);
    std::ios_base::sync_with_stdio(false), std::cin.tie(nullptr), std::cout.tie(nullptr);

    std::vector<std::pair<double, char>> sortedRealFrequency = realFrequency;
    sort(sortedRealFrequency.begin(), sortedRealFrequency.end(), comp);

    // Alphabet -- card([a, b, ..., z]) = 26
    std::string cipher;
    std::cin >> cipher;
    // https://habr.com/ru/post/221485/
    std::size_t keyLength = find_key_length(cipher, 6, 11);
    std::string key = find_key(cipher, keyLength);
    std::string text = decode(cipher, key);

    std::cout << "Text:\n" << text << "\n\n";
    std::cout << "Key:\n" << key << "\n\n";
    std::cout << "Cipher:\n" << cipher << "\n\n";
    std::cout << "Stats:\n";
    printCompare(get_sorted_frequency(text), sortedRealFrequency);
    return 0;
}