#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

const std::string FILE_NAME = "test";

typedef long long ll;

const size_t rep_amount = 3000;

signed main() {
    // freopen((FILE_NAME + ".in").c_str(), "r", stdin);
    // freopen((FILE_NAME + ".out").c_str(), "w", stdout);
    //std::ios_base::sync_with_stdio(false), std::cin.tie(nullptr), std::cout.tie(nullptr);
    std::string ans;
    std::vector<ll> answers(rep_amount);
    for (size_t i = 0; i < rep_amount; ++i) {
        ll a;
        std::cin >> a;
        std::cout << 0 << std::endl;
        std::cin >> ans;
        answers[i] = !((a == 1 && ans == "NO") || (a == 0 && ans == "YES"));
    }

    ll size = rep_amount / 2;
    while (size > 0) {
        bool flag = true;
        for (int i = 0; i < size; ++i) {
            if (answers[i] != answers[size + i]) {
                flag = false;
                break;
            }
        }
        if (flag)
            break;
        --size;
    }

    ll check = rep_amount;
    for (size_t i = 0; i < 10000 - rep_amount; ++i, ++check) {
        ll a;
        std::cin >> a;
        std::cout << (a != answers[check % size]) << std::endl;
        std::cin >> ans;
    }
    return 0;
}