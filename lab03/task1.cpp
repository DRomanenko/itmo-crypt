#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

const std::string FILE_NAME = "test";

typedef long long ll;


signed main() {
    // freopen((FILE_NAME + ".in").c_str(), "r", stdin);
    // freopen((FILE_NAME + ".out").c_str(), "w", stdout);
    //std::ios_base::sync_with_stdio(false), std::cin.tie(nullptr), std::cout.tie(nullptr);
    ll qty_zero = 0;
    std::string ans;
    for (size_t i = 0; i < 1000; ++i) {
        ll a;
        std::cin >> a;
        std::cout << 0 << std::endl;
        std::cin >> ans;
        if ((a == 1 && ans == "NO") || (a == 0 && ans == "YES"))
            ++qty_zero;
    }

    ll check = qty_zero < 500;
    for (size_t i = 0; i < 9000; ++i) {
        ll a;
        std::cin >> a;
        std::cout << (a != check) << std::endl;
        std::cin >> ans;
    }
    return 0;
}