# Cách vận hành gửi và nhận file giữa 2 thiết bị Android

`TransferServer.kt`, quá trình gửi và nhận file giữa 2 thiết bị Android (người gửi và người nhận) hoạt động theo mô hình Client-Server thông qua mạng nội bộ (mạng LAN, WiFi hoặc Mobile Hotspot), trong đó thiết bị **Người gửi (Sender)** đóng vai trò là một máy chủ Web (Web Server cục bộ) và thiết bị **Người nhận (Receiver)** đóng vai trò là Client.

## 1. Phía Người gửi (Sender)
* **Khởi tạo Server:** Thiết bị gửi sẽ khởi tạo một máy chủ HTTP cục bộ bằng framework Ktor (`embeddedServer`) thông qua hàm `TransferServer.startServer(...)`. Nó truyền vào danh sách các file cần chia sẻ (`filesToShare`).
* **Lấy IP và Port:** Server sẽ lắng nghe trên địa chỉ IP cục bộ của thiết bị (được lấy qua hàm `getLocalIpAddress()`) ở một cổng nhất định (mặc định là `8080`). Ví dụ URL kết nối sẽ có dạng: `http://192.168.1.5:8080`.
* **Chia sẻ kết nối (Mã QR):** Để thiết bị nhận có thể biết được địa chỉ IP và Port này, thiết bị gửi cung cấp hàm `generateQRCode()` để tạo ra một mã QR. Thiết bị nhận sẽ dùng camera quét mã QR này để lấy thông tin kết nối.

## 2. Các API được cung cấp trên thiết bị gửi
Máy chủ HTTP trên thiết bị gửi mở các đường dẫn (endpoints) sau để thiết bị nhận có thể truy cập:

* **`/api/info`**: Trả về thông tin cơ bản của thiết bị gửi (ví dụ: tên dòng máy thiết bị - `android.os.Build.MODEL`) dưới dạng JSON.
* **`/api/files`**: Trả về danh sách định dạng JSON của tất cả các file đang được chia sẻ. Thông tin mỗi file bao gồm: tên (`name`), kích thước (`size`), kích thước đã định dạng (`formattedSize`) và đường dẫn để tải file (`downloadUrl`).
* **`/preview/{fileName}`**: Cho phép thiết bị nhận xem trước (preview) nội dung của một file cụ thể (phục vụ hiển thị ảnh, video trực tiếp trên thiết bị nhận).
* **`/download/{fileName}`**: Cho phép thiết bị nhận tải xuống một file cụ thể thông qua tên file. Server trả về nội dung file dưới dạng tệp đính kèm (`Attachment`).
* **`/download-all`**: Cung cấp khả năng tải toàn bộ file cùng một lúc. Khi gọi API này, máy chủ sẽ tự động nén tất cả file cần chia sẻ vào một file `.zip` (`createZipFile`) rồi trả file zip đó về. File zip này sẽ tự động bị xóa ở máy gửi sau 5 giây để giải phóng bộ nhớ.
* **`/api/refuse`**: API dành cho thiết bị nhận gửi tín hiệu từ chối việc nhận file. Khi được gọi, máy gửi sẽ nhận được callback `onRefused`.

## 3. Phía Người nhận (Receiver)
Thiết bị nhận (Receiver) đóng vai trò như một HTTP Client (hoặc trình duyệt):
1. **Kết nối:** Quét mã QR từ màn hình của người gửi để lấy được địa chỉ URL (IP và Port).
2. **Khám phá:** Gọi đến `/api/info` để hiển thị tên thiết bị gửi và `/api/files` để lấy thông tin danh sách các file sẽ được nhận.
3. **Thực thi:**
    - Tiến hành gọi API `/download/{fileName}` để tải từng file riêng lẻ.
    - Hoặc gọi API `/download-all` để tải về tất cả cùng lúc dưới dạng nén ZIP qua kết nối mạng nội bộ tốc độ cao.
4. Quá trình tải hoàn tất, cả hai bên có thể sử dụng các callback (`onFileDownloaded`) để cập nhật giao diện người dùng (như thanh tiến trình, hiển thị file đã lưu).

# Cách vận hành lưu lịch sử truyền file (History)

Quá trình lưu lại lịch sử truyền nhận file trên thiết bị gửi và nhận được quản lý tập trung trong `TransferViewModel` và sử dụng **Room Database** để lưu trữ cục bộ.

## 1. Cơ sở dữ liệu (Database) được sử dụng
* **Công nghệ:** Ứng dụng sử dụng **Room Database**, một thư viện thuộc Android Architecture Components cung cấp một lớp trừu tượng trên SQLite.
* **Tên Database:** `data_transfer_db` (được định nghĩa trong `DatabaseModule.kt`).
* **Entity và DAO:** Sử dụng entity `TransferHistory` để lưu thông tin về mỗi lần chuyển file (bao gồm tên file, kích thước, kiểu gửi/nhận, đường dẫn file). Các thao tác Thêm/Sửa/Xóa/Đọc được định nghĩa trong `HistoryDao`.

## 2. Lưu lịch sử ở thiết bị Gửi (Sender)
Thiết bị gửi sẽ lưu lịch sử với trường `type = "SEND"`. Quá trình lưu diễn ra ở 2 trường hợp:
* **Khi truyền qua QR Code (Web Server):** Khi khởi chạy server bằng `TransferServer.startServer`, có một callback là `onFileDownloaded`. Mỗi khi thiết bị nhận tải xong một file từ server, server sẽ kích hoạt callback này và `TransferViewModel` sẽ gọi hàm `saveHistory` để ghi lại file đó vào Room database.
* **Khi truyền qua Network Service Discovery (Device / WiFi Direct):** Trong hàm `StartSendFileDevice()`, ứng dụng sẽ lặp qua danh sách file đã chọn và gọi `PeerFileSender.sendFile`. Nếu tiến trình gửi file thành công (`success = true`), nó lập tức gọi hàm `saveHistory` cho file đó.

## 3. Lưu lịch sử ở thiết bị Nhận (Receiver)
Thiết bị nhận sẽ lưu lịch sử với trường `type = "RECEIVE"`. Quá trình lưu diễn ra như sau:
* **Khi tải file qua HTTP (quét QR / Web Server):** Trong `TransferViewModel`, khi người nhận tiến hành gọi hàm `downloadFileInternal()`, file được tải về stream, ghi vào bộ nhớ máy thông qua `saveFile()`. Sau khi lưu file thành công ra bộ nhớ lưu trữ (có được `Uri` đích), ứng dụng gọi hàm `saveHistory` để chèn thông tin lịch sử tải xuống.
* **Khi nhận file qua NSD (Device):** `TransferViewModel` lắng nghe luồng sự kiện `PeerFileSender.receivedFiles`. Mỗi khi nhận được một luồng file mới từ máy gửi, ứng dụng kiểm tra bộ đệm (`globalSavedReceivedFiles`) để chống trùng lặp, sau đó gọi `saveHistory` và lưu vào Room.

## 4. Cách truy xuất và hiển thị (MyFileScreen)
* Trên giao diện `MyFileScreen`, màn hình sẽ quan sát (collect) một luồng dữ liệu (Flow) `historyEntries` từ `MyFileViewModel`.
* `historyEntries` chính là danh sách được lấy trực tiếp từ `historyDao.getAllHistory()`.
* Vì Room hỗ trợ Flow, nên mỗi khi có bất kỳ file nào được gửi hoặc nhận thành công (dữ liệu chèn vào DB), giao diện `MyFileScreen` sẽ tự động cập nhật danh sách lịch sử theo thời gian thực mà không cần gọi lại API đọc database.

#
