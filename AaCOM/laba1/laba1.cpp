#include <iostream>
#include <fstream>
#include <vector>
#include <iomanip>
#include <cmath>
#include <algorithm>
#include <numeric>

using namespace std;

struct Fraction {
    long long numerator;
    long long denominator;

    Fraction(long long num = 0, long long denom = 1) : numerator(num), denominator(denom) {
        if (denominator == 0) {
            throw invalid_argument("Знаменатель не может быть равен 0");
        }
        simplify();
    }

    void simplify() {
        long long gcd_val = gcd(abs(numerator), abs(denominator));//НОД
        numerator /= gcd_val;
        denominator /= gcd_val;
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
    }

    friend ostream& operator<<(ostream& os, const Fraction& frac) {
        if (frac.denominator == 1) {
            os << frac.numerator;
        } else {
            os << frac.numerator << "/" << frac.denominator;
        }
        return os;
    }

    // Операторы для работы с дробями
    Fraction operator+(const Fraction& other) const {
        return Fraction(numerator * other.denominator + other.numerator * denominator, denominator * other.denominator);
    }

    Fraction operator-(const Fraction& other) const {
        return Fraction(numerator * other.denominator - other.numerator * denominator, denominator * other.denominator);
    }

    Fraction operator*(const Fraction& other) const {
        return Fraction(numerator * other.numerator, denominator * other.denominator);
    }

    Fraction operator/(const Fraction& other) const {
        return Fraction(numerator * other.denominator, denominator * other.numerator);
    }
};


vector<vector<Fraction>> readMatrix(const string& filename) {
    ifstream file(filename);
    if (!file.is_open()) {
        cerr << "Ошибка открытия файла!" << endl;
        exit(1);
    }

    vector<vector<Fraction>> matrix;
    string line;
    while (getline(file, line)) {
        vector<Fraction> row;
        double value;
        istringstream iss(line);
        while (iss >> value) {
            row.emplace_back(static_cast<long long>(value * 1000), 1000); // Преобразуем в дробь
        }
        matrix.push_back(row);
    }

    file.close();
    return matrix;
}


void printMatrix(const vector<vector<Fraction>>& matrix) {
    for (const auto& row : matrix) {
        for (const auto& value : row) {
            cout << setw(10) << value << " ";
        }
        cout << endl;
    }
    cout << endl;
}

void gaussJordan(vector<vector<Fraction>>& matrix) {
    int m = matrix.size();
    if (m == 0) {
        cout << "Матрица пуста" << endl;
        return;
    }
    int n = matrix[0].size() - 1;
    int k = 0;
    vector<int> leadingCols; //для индексов ведущих столбцов
    int step = 1;

    for (int col = 0; col < n; ++col) {
        if (k >= m) break;

        int maxRow = k;
        Fraction maxVal = matrix[k][col];
        for (int i = k + 1; i < m; ++i) {
            if (abs(matrix[i][col].numerator) > abs(maxVal.numerator)) {
                maxVal = matrix[i][col];
                maxRow = i;
            }
        }

        if (maxVal.numerator == 0) continue;

        if (maxRow != k) {
            swap(matrix[k], matrix[maxRow]);
        }

        Fraction divisor = matrix[k][col];
        for (int j = col; j < n + 1; ++j) {
            matrix[k][j] = matrix[k][j] / divisor;
        }

        for (int i = 0; i < m; ++i) {
            if (i != k) {
                Fraction factor = matrix[i][col];
                for (int j = col; j < n + 1; ++j) {
                    matrix[i][j] = matrix[i][j] - factor * matrix[k][j];
                }
            }
        }

        cout << "После шага " << step << ":" << endl;
        printMatrix(matrix);
        step++;

        leadingCols.push_back(col);
        k++;
    }

    // Проверка на противоречия
    bool hasInconsistent = false;
    for (int i = 0; i < m; ++i) {
        bool allZero = true;
        for (int j = 0; j < n; ++j) {
            if (matrix[i][j].numerator != 0) {
                allZero = false;
                break;
            }
        }
        if (allZero && matrix[i][n].numerator != 0) {
            hasInconsistent = true;
            break;
        }
    }

    if (hasInconsistent) {
        cout << "Система не имеет решений" << endl;
        return;
    }

    int numLeading = leadingCols.size();
    if (numLeading == n) {
        vector<Fraction> solution(n);
        for (int i = 0; i < numLeading; ++i) {
            int col = leadingCols[i];
            solution[col] = matrix[i][n];
        }
        cout << "Единственное решение:" << endl;
        for (int i = 0; i < n; ++i) {
            cout << "x" << i << " = " << solution[i] << endl;
        }
    } else {
        vector<int> freeVars;
        for (int j = 0; j < n; ++j) {
            if (find(leadingCols.begin(), leadingCols.end(), j) == leadingCols.end()) {
                freeVars.push_back(j);
            }
        }
        cout << "Система имеет бесконечно много решений" << endl;
        cout << "Свободные переменные: ";
        for (int j : freeVars) {
            cout << "x" << j << " ";
        }
        cout << endl;

        for (int i = 0; i < numLeading; ++i) {
            int leadCol = leadingCols[i];
            string expr = "x" + to_string(leadCol) + " = " + to_string(matrix[i][n].numerator) + "/" + to_string(matrix[i][n].denominator);
            for (int fv : freeVars) {
                Fraction coeff = matrix[i][fv];
                if (coeff.numerator != 0) {
                    string sign = (coeff.numerator > 0) ? "-" : "+";
                    expr += " " + sign + " " + to_string(abs(coeff.numerator)) + "/" + to_string(coeff.denominator) + "*x" + to_string(fv);
                }
            }
            cout << expr << endl;
        }
    }
}

int main() {
    string filename = "matrix.txt";
    vector<vector<Fraction>> matrix = readMatrix(filename);
    cout << "Исходная матрица:" << endl;
    printMatrix(matrix);

    gaussJordan(matrix);

    return 0;
}
