import sqlite3

def check_db():
    conn = sqlite3.connect('database/bansos.db')
    cur = conn.cursor()
    
    print("\n[DAFTAR TABEL]")
    for row in cur.execute("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'"):
        print("-", row[0])
        
    print("\n[DATA USERS]")
    for row in cur.execute("SELECT id, username, nama_lengkap, role FROM users"):
        print(f"User {row[0]}: {row[1]} ({row[2]}) - Role: {row[3]}")
        
    print("\n[DATA KRITERIA SAW]")
    for row in cur.execute("SELECT kode, nama, atribut, bobot FROM kriteria_saw"):
        print(f"{row[0]}: {row[1]} [{row[2]}] -> Bobot: {row[3]}%")
        
    conn.close()

if __name__ == '__main__':
    check_db()
