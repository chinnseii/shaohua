use stzb; 

drop table if exists user; 

create table if not exists user ( 
    id int primary key auto_increment comment 'id'
    , email varchar (320) not null comment '邮箱'
    , password varchar (255) not null comment '密码'
    , lock_flg int comment '账号是否冻结'
    , login_time datetime comment '登陆时间'
    , create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间'
    , update_time datetime DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
); 

drop table if exists userinfo; 

create table if not exists userinfo( 
    email varchar (320) not null primary key comment '邮箱'
    , avatar_path varchar (255) not null comment '头像路径'
    , signature varchar (255) comment '个性签名'
    , message int comment '消息'
    , nick_name varchar (20) not null comment '游戏名'
    , point int comment '个人总积分'
    , point_last int comment '剩余积分'
    , alliance_id int ZEROFILL comment '盟id'
    , alliance_name varchar (10) comment '盟名称'
    , group_id int comment '团id'
    , group_name varchar (10) comment '团名称'
    , jurisdiction int comment '权限'
    , create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间'
    , update_time datetime DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
); 

drop table if exists alliance; 

create table if not exists alliance( 
    alliance_Id int ZEROFILL primary key auto_increment comment '盟id'
    , own_email varchar (320) not null comment '盟主邮箱'
    , own_name varchar (320) not null comment '盟主游戏名'
    , name varchar (20) not null comment '盟名称'
    , introduce varchar (50) comment '盟介绍'
    , population int not null comment '盟人口'
    , create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间'
    , update_time datetime DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
); 

drop table if exists groupmaster; 

create table if not exists groupmaster( 
    groupid int primary key auto_increment comment '团id'
    , alliance_id int not null comment '盟id'
    , leader_id int comment '团长ID'
    , leader_name varchar (20) not null comment '团长昵称'
    , group_name varchar (20) not null comment '团名称'
    , group_introduce varchar (50) comment '团介绍'
    , population int not null comment '团人口'
    , create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间'
    , update_time datetime DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
); 

drop table if exists task; 

CREATE TABLE if not exists task( 
    id int PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '任务id'
    , title varchar (50) NOT NULL COMMENT '任务名'
    , content varchar (500) NOT NULL COMMENT '任务内容'
    , own int NOT NULL COMMENT '任务发布者id'
    , extent int NOT NULL COMMENT '盟级0团级1'
    , Implementerid int NOT NULL COMMENT '任务实施者'
    , status int NOT NULL COMMENT '状态'
    , x int COMMENT '任务地点X坐标'
    , y int COMMENT '任务地点Y坐标'
    , bonus int COMMENT '任务达成积分'
    , start_time date comment '任务开始时间'
    , end_time date comment '任务截至时间'
    , create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间'
    , update_time datetime DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
); 

drop table if exists emailmaster; 

create table if not exists emailmaster( 
    id int primary key AUTO_INCREMENT comment '邮件id'
    , title varchar (50) comment '邮件标题'
    , content varchar (10000) not null comment '邮件内容'
    , create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间'
    , update_time datetime DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
); 


drop table if exists application; 

create table if not exists application( 
    id int primary key AUTO_INCREMENT comment '申请ID'
    , email varchar (320) not null comment '申请人邮箱'
    , nick_name varchar (20) not null comment '申请人昵称'
    , alliance_id int ZEROFILL comment '盟id'
    , group_id int comment '团id'
    , type int not null comment '申请种类'
    , create_time datetime DEFAULT CURRENT_TIMESTAMP comment '申请时间'
    , status int not null comment '受理状态'
    , process_result int not null comment '处理结果'
    , update_time datetime DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
);
import requests
from bs4 import BeautifulSoup
import json
from datetime import datetime
import re  # 导入正则表达式模块

def parse_shinchiku_page(soup):
    """解析 https://suumo.jp/ms/shinchiku/tokyo/ensen/ 页面特有的结构"""
    result = []
    section_containers = soup.find_all('div', class_='ui-section--h2')
    
    for container in section_containers:
        body_div = container.find('div', class_='ui-section-body')
        if not body_div:
            continue
        
        # 针对 shinchiku 页面中 area_unit 结构进行解析
        area_units = body_div.find_all('div', class_='area_unit')
        for area_unit in area_units:
            title = area_unit.find('div', class_='area_unit-title').find('label').get_text(strip=True) if area_unit.find('div', class_='area_unit-title') else None
            items = []
            area_body = area_unit.find('div', class_='area_unit-body')
            if area_body:
                stripe_lists = area_body.find_all('ul', class_='stripe_lists-line')
                for stripe_list in stripe_lists:
                    li_elements = stripe_list.find_all('li')
                    for li in li_elements:
                        label = li.find('label')
                        if label:
                            # 获取 name 和 amount，即使数量为零
                            name = label.get_text(strip=True)  # 获取 label 内的文本内容
                            name = re.sub(r"\(.*\)", "", name)  # 使用正则去除括号及括号中的内容
                            amount_span = label.find('span')
                            amount = amount_span.get_text(strip=True).strip("()") if amount_span else "0"  # 提取数量，默认为 0
                            items.append({
                                "name": name.strip(),  # 去掉可能的多余空格
                                "amount": amount
                            })
            if title and items:
                result.append({
                    "title": title,
                    "options": items
                })
    return result

def parse_other_page(soup):
    """解析其他类型的页面结构"""
    result = []
    section_containers = soup.find_all('div', class_='ui-section--h2')

    for container in section_containers:
        body_div = container.find('div', class_='ui-section-body')
        if not body_div:
            continue
        search_tables = body_div.find_all('table', class_='searchtable')
        for table in search_tables:
            rows = table.find_all('tr')
            for row in rows:
                # 提取标题
                title = row.find('th', class_='searchtable-title').get_text(strip=True) if row.find('th', class_='searchtable-title') else None
                
                # 提取选项信息
                items = []
                td = row.find('td', class_='searchtable-box')
                if td:
                    li_elements = td.find_all('li')
                    for li in li_elements:
                        label = li.find('label')
                        if label:
                            # 获取选项名称和数量
                            name = label.contents[0].strip()  # 获取标签文本的第一个部分
                            amount_span = label.find('span', class_='searchitem-list-value')
                            amount = amount_span.get_text(strip=True).strip("()") if amount_span else "0"
                            items.append({
                                "name": name,
                                "amount": amount
                            })

                # 将标题和选项存入结果
                if title and items:
                    result.append({
                        "title": title,
                        "options": items
                    })

    return result

def scrape_custom_searchtable_to_json(url):
    try:
        # 发起请求
        headers = {'User-Agent': 'Mozilla/5.0'}
        response = requests.get(url, headers=headers)
        response.raise_for_status()
        html_content = response.text

        # 使用 BeautifulSoup 解析网页内容
        soup = BeautifulSoup(html_content, 'html.parser')

        # 提取页面中 h1 标题内容
        h1_element = soup.find('h1', class_='ui-section-title')
        if not h1_element:
            print(f"未找到 class 为 'ui-section-title' 的 <h1>，跳过 URL: {url}")
            return
        page_title = h1_element.get_text(strip=True)

        # 获取今天的日期
        today_date = datetime.today().strftime('%Y-%m-%d')

        # 拼接文件名
        sanitized_title = page_title.replace(" ", "_").replace("・", "_").replace("／", "_").replace("｜", "_").replace("/", "_")
        output_file = f"{sanitized_title}_{today_date}.json"

        # 根据不同页面调用不同解析方法
        if "ms/shinchiku" in url:
            result = parse_shinchiku_page(soup)
        else:
            result = parse_other_page(soup)

        # 保存结果到 JSON 文件
        with open(output_file, 'w', encoding='utf-8') as json_file:
            json.dump(result, json_file, ensure_ascii=False, indent=4)

        print(f"成功提取 {len(result)} 个表单内容，结果已保存到 {output_file}")

    except requests.exceptions.RequestException as e:
        print(f"请求失败: {e}")
    except Exception as e:
        print(f"发生错误: {e}")

def read_urls_from_file(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            urls = [line.strip() for line in file.readlines() if line.strip()]
        return urls
    except FileNotFoundError:
        print(f"文件 {file_path} 未找到")
        return []
    except Exception as e:
        print(f"读取文件时发生错误: {e}")
        return []

if __name__ == "__main__":
    # 从 url.conf 文件读取所有要爬取的 URL
    urls = read_urls_from_file('url.conf')
    
    if urls:
        for url in urls:
            scrape_custom_searchtable_to_json(url)
    else:
        print("未找到任何 URL，程序结束。")

