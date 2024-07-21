from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
import time
from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager
from chromedriver_py import binary_path
import json
import requests



# Thiết lập tùy chọn cho Chrome
chrome_options = Options()
chrome_options.add_argument("--headless")  # Chạy trình duyệt ở chế độ không giao diện
svc = webdriver.ChromeService(executable_path=binary_path)
driver = webdriver.Chrome(service=svc)
# URL của trang cửa hàng
store_url = 'https://www.lazada.vn/coolmate/?q=All-Products&from=wangpu&langFlag=en&pageTypeId=2'  # Thay đổi URL này với URL cửa hàng bạn muốn lấy dữ liệu

data = []
try:
    # Mở trang web
    driver.get(store_url)
    
    # Chờ cho trang tải xong
    time.sleep(10)

    # Lấy tất cả các phần tử sản phẩm (Cập nhật query selector theo cấu trúc thực tế)
    product_elements = driver.find_elements(By.CSS_SELECTOR, '.Bm3ON')  # Thay đổi selector này

    for product in product_elements:
        # Lấy tên sản phẩm
        try:
            product_name = product.find_element(By.CSS_SELECTOR, '.RfADt').text  # Thay đổi selector này
        except:
            product_name = "N/A"

        # Lấy giá sản phẩm
        try:
            product_price = product.find_element(By.CSS_SELECTOR, '.ooOxS').text  # Thay đổi selector này
        except:
            product_price = "N/A"

        try:
            img_element = product.find_element(By.CSS_SELECTOR, 'img')  # Tìm phần tử img trong div
            img_url = img_element.get_attribute('src')  # Lấy URL của ảnh
        except Exception as e:
            print(f"Không tìm thấy ảnh trong phần tử: {e}")
        
        data.append({
            'name': product_name,
            'price': product_price,
            'image': img_url
        })
        print(f"Name: {product_name}\nPrice: {product_price}\nImage: {img_url}\n")

    # Lấy dữ liệu thêm nếu có nhiều trang (cập nhật logic nếu cần)
    # Ví dụ: cuộn trang hoặc nhấp nút "Xem thêm"
    # Chuyển dữ liệu thành JSON
    json_data = json.dumps(data, ensure_ascii=False)
    print(json_data)

    # # Gửi dữ liệu đến server
    # headers = {'Content-Type': 'application/json'}
    # response = requests.post("http://localhost:8080/getData", data=json_data, headers=headers)

    # # Kiểm tra phản hồi từ server
    # if response.status_code == 200:
    #     print("Dữ liệu đã được gửi thành công.")
    # else:
    #     print(f"Có lỗi xảy ra: {response.status_code}")
    # time.sleep(60)
finally:
    # Đóng trình duyệt
    driver.quit()


