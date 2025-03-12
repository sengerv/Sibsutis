from itertools import combinations
class Fraction:
    def __init__(self, numerator, denominator=1):
        if denominator == 0:
            raise ValueError("Denominator cannot be zero")
        self.numerator = numerator
        self.denominator = denominator
        self.simplify()
    
    def simplify(self):
        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a
        g = gcd(abs(self.numerator), abs(self.denominator))
        self.numerator //= g
        self.denominator //= g
        if self.denominator < 0:
            self.numerator = -self.numerator
            self.denominator = -self.denominator
    
    def __add__(self, other):
        num = self.numerator * other.denominator + other.numerator * self.denominator
        den = self.denominator * other.denominator
        return Fraction(num, den)
    
    def __sub__(self, other):
        num = self.numerator * other.denominator - other.numerator * self.denominator
        den = self.denominator * other.denominator
        return Fraction(num, den)
    
    def __mul__(self, other):
        return Fraction(self.numerator * other.numerator, self.denominator * other.denominator)
    
    def __truediv__(self, other):
        return Fraction(self.numerator * other.denominator, self.denominator * other.numerator)
    
    def __abs__(self):
        return Fraction(abs(self.numerator), abs(self.denominator))
    
    def __eq__(self, other):
        return self.numerator == other.numerator and self.denominator == other.denominator
    
    def __ne__(self, other):
        return not self.__eq__(other)
    
    def __lt__(self, other):
        return self.numerator * other.denominator < other.numerator * self.denominator
    
    def __le__(self, other):
        return self.numerator * other.denominator <= other.numerator * self.denominator
    
    def __gt__(self, other):
        return self.numerator * other.denominator > other.numerator * self.denominator
    
    def __ge__(self, other):
        return self.numerator * other.denominator >= other.numerator * self.denominator
    
    def __repr__(self):
        if self.denominator == 1:
            return f"{self.numerator}"
        return f"{self.numerator}/{self.denominator}"
    

class FractionMatrix:
    def __init__(self, matrix):
        self.matrix = [[Fraction(cell) for cell in row] for row in matrix]
        self.rows = len(matrix)
        self.cols = len(matrix[0])
    
    def swap_rows(self, i, j):
        self.matrix[i], self.matrix[j] = self.matrix[j], self.matrix[i]
    
    def divide_row(self, i, divisor):
        self.matrix[i] = [cell / divisor for cell in self.matrix[i]]
    
    def subtract_rows(self, i, j, factor):
        self.matrix[i] = [a - factor * b for a, b in zip(self.matrix[i], self.matrix[j])]
    
    def find_pivot(self, col):
        pivot_row = max(range(col, self.rows), key=lambda i: abs(self.matrix[i][col]))
        return pivot_row if self.matrix[pivot_row][col] != Fraction(0) else -1

    def read_matrix(filename):
        def parse_number(s):
            if '/' in s:
                num, den = s.split('/')
                return Fraction(int(num), int(den))
            else:
                return Fraction(int(s))
        
        with open(filename, 'r') as f:
            lines = f.readlines()
        matrix = []
        for line in lines:
            row = [parse_number(x) for x in line.strip().split()]
            matrix.append(row)
        return matrix
    
def print_matrix(matrix):
    for row in matrix:
        print(' '.join([f"{x}" for x in row]))
    print()

def read_matrix(filename):
    def parse_number(s):
        if '/' in s:
            num, den = s.split('/')
            return Fraction(int(num), int(den))
        else:
            return Fraction(int(s))
    
    with open(filename, 'r') as f:
        lines = f.readlines()
    matrix = []
    for line in lines:
        row = [parse_number(x) for x in line.strip().split()]
        matrix.append(row)
    return matrix

def gauss_jordan(matrix):
    m = len(matrix)
    if m == 0:
        print("Matrix is empty")
        return
    
    o_matrix = [row.copy() for row in matrix]
    n = len(matrix[0]) - 1
    k = 0
    lead_columms = []
    step = 1

    for col in range(n):
        if k >= m: break

        # max элемент в столбце
        max_row = k
        max_val = abs(matrix[k][col])
        for i in range(k, m):
            current = abs(matrix[i][col])
            if current > max_val:
                max_val = current
                max_row = i

        if max_val.numerator == 0:
            continue

        if max_row != k:
            matrix[k], matrix[max_row] = matrix[max_row], matrix[k]

        # ведущая строка
        divisor = matrix[k][col]
        for j in range(col, n+1):
            matrix[k][j] = matrix[k][j] / divisor

        # исключение переменной из др. строк
        for i in range(m):
            if i != k:
                factor = matrix[i][col]
                for j in range(col, n+1):
                    matrix[i][j] = matrix[i][j] - factor * matrix[k][j]
        print(f"Step {step}:")
        print_matrix(matrix)
        step += 1
        lead_columms.append(col)
        k += 1

    # проверка на противоречия
    for row in matrix:
        if all(x.numerator == 0 for x in row[:-1]) and row[-1].numerator != 0:
            print("No solution")
            return
    # определение ранга
    rank = len(lead_columms) # список ведущих столбцов
    if rank == 0:
        print("All variables are free")
        return

    # поиск всех базисных решений
    print("\nBasis solutions:")
    # свободные переменные
    free_vars = [j for j in range(n) if j not in lead_columms]
    
    # Если система определённая
    if rank == n:
        solution = [Fraction(0, 1) for _ in range(n)]
        for i, col in enumerate(lead_columms):
            solution[col] = matrix[i][n]
        print("One basis solutions:")
        for i in range(n):
            print(f"x{i} = {solution[i]}")
    else:
        # генерация всех возможных комбинаций переменных размером с ранг системы
        for basis in combinations(lead_columms + free_vars, rank):  
            basis = sorted(basis)
            if not is_independent(o_matrix, basis):
                continue
            
            try:
                solution = get_basis_solution(o_matrix, basis, n)
                print(f"Basis variables: {basis}")
                for i in range(n):
                    print(f"x{i} = {solution[i]}")
                print()
            except:
                continue

def is_independent(matrix, basis):
    # проверка линейной независимости столбцов
    temp = [[row[col] for col in basis] for row in matrix]
    rank = 0
    n_cols = len(basis)
    # Гаус
    for col in range(n_cols):
        pivot = None            # ищем ненулевой элемент
        for row in range(rank, len(temp)):
            if temp[row][col].numerator != 0:
                pivot = temp[row][col]
                break
        if pivot is None:
            print(f"Комбинация {basis} линейно зависима: отсутствует ненулевой элемент в столбце {col}\n")
            return False        # ненулевой элемент не нашли
        pivot_row = temp[row]
        temp[row], temp[rank] = temp[rank], temp[row]
        for i in range(len(temp)):                      # исключение и нормировка
            if i != rank and temp[i][col].numerator != 0:
                factor = temp[i][col] / pivot
                temp[i] = [temp[i][j] - factor * pivot_row[j] for j in range(n_cols)]
        rank += 1
    return rank == n_cols        # возвращаем линено независимые столбцы

def get_basis_solution(matrix, basis, n_vars):
    m = len(matrix)
    n = n_vars
    sys_matrix = []     # создаём расширенную матрицу для базиса
    for row in matrix:
        new_row = [row[col] for col in basis] + [row[-1]]
        sys_matrix.append(new_row)
    
    for i in range(len(basis)):     # gauss
        pivot_row = max(range(i, m), key=lambda r: abs(sys_matrix[r][i]))   # поиск ведущего элемента
        if sys_matrix[pivot_row][i].numerator == 0:
            raise ValueError("Columns are linearly dependent")
        
        sys_matrix[i], sys_matrix[pivot_row] = sys_matrix[pivot_row], sys_matrix[i]
        pivot = sys_matrix[i][i]
        
        # нормировка
        for j in range(i, len(basis)+1):
            sys_matrix[i][j] = sys_matrix[i][j] / pivot
        
        # исключение
        for k in range(m):
            if k != i:
                factor = sys_matrix[k][i]
                for j in range(i, len(basis)+1):
                    sys_matrix[k][j] = sys_matrix[k][j] - factor * sys_matrix[i][j]
    
    # формируем решение
    solution = [Fraction(0, 1) for _ in range(n)]
    for i, col in enumerate(basis):
        solution[col] = sys_matrix[i][-1]
    
    return solution

def main():
    filename = "4.txt"
    try:
        matrix = read_matrix(filename)
        print("Original matrix:")
        print_matrix(matrix)
        gauss_jordan(matrix)
    except Exception as e:
        print(f"Error: {str(e)}")

if __name__ == "__main__":
    main()