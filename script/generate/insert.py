import random
from datetime import datetime, timedelta


# Function to generate random strings
def random_name():
    names = ['Emma', 'Olivia', 'Ava', 'Isabella', 'Sophia', 'Charlotte', 'Mia', 'Amelia', 'Harper', 'Evelyn', 'Abigail', 'Emily', 'Ella', 'Elizabeth', 'Camila', 'Luna', 'Sofia', 'Avery', 'Mila', 'Aria', 'Scarlett', 'Penelope', 'Layla', 'Chloe', 'Victoria', 'Madison', 'Eleanor', 'Grace', 'Nora', 'Riley', 'Zoey', 'Hannah', 'Hazel', 'Lily', 'Ellie', 'Violet', 'Lillian', 'Zoe', 'Stella', 'Aurora', 'Natalie', 'Emilia', 'Everly', 'Leah', 'Willow', 'Addison', 'Lucy', 'Audrey', 'Bella', 'Nova', 'Brooklyn', 'Paisley', 'Savannah', 'Claire', 'Skylar', 'Isla', 'Genesis', 'Naomi', 'Elena', 'Caroline', 'Eliana', 'Anna', 'Maya', 'Valentina', 'Ruby', 'Kennedy', 'Ivy', 'Ariana', 'Aaliyah', 'Cora', 'Madelyn', 'Alice', 'Kinsley', 'Hailey', 'Gabriella', 'Allison', 'Gianna', 'Serenity', 'Samantha', 'Sarah', 'Autumn', 'Quinn', 'Eva', 'Piper', 'Sophie', 'Sadie', 'Delilah', 'Josephine', 'Nevaeh', 'Adeline', 'Arya', 'Emery', 'Lydia', 'Clara', 'Vivian', 'Liam', 'Noah', 'Oliver', 'William', 'Elijah', 'James', 'Benjamin', 'Lucas', 'Henry', 'Alexander', 'Mason', 'Michael', 'Ethan', 'Daniel', 'Jacob', 'Logan', 'Jackson', 'Levi', 'Sebastian', 'Mateo', 'Jack', 'Owen', 'Theodore', 'Aiden', 'Samuel', 'Joseph', 'John', 'David', 'Wyatt', 'Matthew', 'Luke', 'Asher', 'Carter', 'Julian', 'Grayson', 'Leo', 'Jayden', 'Gabriel', 'Isaac', 'Lincoln', 'Anthony', 'Hudson', 'Dylan', 'Ezra', 'Thomas', 'Charles', 'Christopher', 'Jaxon', 'Maverick', 'Josiah', 'Isaiah', 'Andrew', 'Elias', 'Joshua', 'Nathan', 'Caleb', 'Ryan', 'Adrian', 'Miles', 'Eli', 'Nolan', 'Christian', 'Aaron', 'Cameron', 'Ezekiel', 'Colton', 'Luca', 'Landon', 'Hunter', 'Jonathan', 'Santiago', 'Axel', 'Easton', 'Cooper', 'Jeremiah', 'Angel', 'Roman', 'Connor', 'Jameson', 'Robert', 'Greyson', 'Jordan', 'Ian', 'Carson', 'Adam', 'Jaxson', 'Brooks', 'Nathaniel', 'Jace', 'Derek', 'Kyle', 'Brandon', 'Blake', 'Peter']
    return random.choice(names)

def generate_username():
    # Списки для создания имени пользователя
    prefixes = ['cool', 'hot', 'super', 'mega', 'awesome', 'crazy', 'funky', 'fun', 'epic', 'fantastic']
    suffixes = ['girl', 'boy', 'dude', 'chick', 'guy', 'ninja', 'master', 'lover', 'star', 'genius']

    # Случайный выбор префикса и суффикса
    prefix = random.choice(prefixes)
    suffix = random.choice(suffixes)

    # Создание имени пользователя
    return f"{prefix}_{suffix}"

def random_hair_color():
    hair_colors = ['black', 'brown', 'blonde', 'red', 'gray', 'white']
    return random.choice(hair_colors)

def generate_random_date():
    start_date = datetime(1960, 1, 1)
    end_date = datetime(2005, 1, 1)
    random_days = random.randint(0, (end_date - start_date).days)
    random_date = start_date + timedelta(days=random_days)
    return random_date.strftime('%Y-%m-%d')

# Function to generate random values for the columns
def generate_insert_statements(table, num_rows):
    insert_statements = []
    user_data_id = 1
    user_spacesuit_data_id = 1

    for _ in range(num_rows):
        if table == 'user_spacesuit_data':
            insert_statements.append(f"INSERT INTO user_spacesuit_data (head, status, chest, waist, hips, foot_size, height, fabric_texture_id) VALUES "
                                     f"({random.randint(30, 80)}, '{random.choice(['DECLINED', 'READY', 'IN_PROGRESS'])}', {random.randint(30, 100)}, "
                                     f"{random.randint(30, 100)}, {random.randint(30, 100)}, {random.randint(30, 50)}, {random.randint(130, 205)}, "
                                     f"{random.randint(1, 5)});")


        elif table == 'user_data':

            insert_statements.append(
                f"INSERT INTO user_data (birth_date, sex, weight, height, hair_color, location, firstname) VALUES "
                f"('{generate_random_date()}', '{random.choice(['MEN', 'WOMEN'])}', {random.randint(40, 150)}, "
                f"{random.randint(130, 205)}, '{random_hair_color()}', '{random.choice(['EARTH', 'MARS'])}', '{random_name()}');")
        elif table == 'users':
            if random.random() < 0.7:
                user_spacesuit_data_id_str = 'NULL'
            else:
                user_spacesuit_data_id_str = str(user_spacesuit_data_id)
                user_spacesuit_data_id += 1
            insert_statements.append(
                f"INSERT INTO users (username, password, user_spacesuit_data_id, user_data_id) VALUES "
                f"('{generate_username()}', '123456', {user_spacesuit_data_id_str}, {user_data_id});")
            user_data_id += 1
    return insert_statements




table_userspacesuit = ['user_spacesuit_data']

tables_data = ['user_data', 'users']
for table in tables_data + table_userspacesuit:  # Объединяем списки таблиц
    if table == 'user_spacesuit_data':  # Добавляем блок для таблицы "user_spacesuit_data"
        insert_statements_900 = generate_insert_statements(table, 900)
        with open(f"{table}.sql", "w") as file:
            file.write("\n".join(insert_statements_900))
    else:
        insert_statements_2000 = generate_insert_statements(table, 2000)
        with open(f"{table}.sql", "w") as file:
            file.write("\n".join(insert_statements_2000))

