# DramaListDemo
這是供 Choco TV 使用的範例 APP。它會從網路上的資料來源下載戲劇列表，將資訊顯示出來供使用者查看。

## 注意事項
為保護資料來源，部分網址已被隱藏並且並未上傳至 Github。如果要順利編譯與使用，請在字串資源檔中新增兩個項目
```
<resources>
    <string name="data_source_url" translatable="false">http://www.somewebsite.com/path/to/data/</string>
    <string name="data_source_path" translatable="false">abcdefg1234567</string>
</resources>
```
其中，data_source_url 是個 http 網址，請將路徑完整填入，並以斜線'/'作為結尾。data_source_path 是一串包含英文與數字的，類似 id 的字串。完整資料來源網址是這兩個字串結合而成。

## 需求與實現方式
此範例 APP 包含兩個頁面。主頁面為戲劇列表，含有每一部戲劇的縮圖、名稱、評分與出版日期。點選任何一部戲劇後，可以進入該劇的詳細資訊頁面，含有戲劇的縮圖、名稱、評分、觀看次數與出版日期等。

### 獲取並顯示列表
使用 retrofit 透過 http GET 方式取得 json 格式的戲劇列表，並用 gson 將其轉換為 java 物件。這些資訊使用 recyclerview 呈現。縮圖部分，則是使用 Glide 套件，自特定網址（包含在戲劇列表的 json 文件中）下載。

### 暫存戲劇列表
成功下載戲劇列表後，會將其存放在本地資料庫中。這部分使用 Android Architecture Component 中的 Room 套件進行插入、更新與讀取的操作。任何時間使用者開啟 APP 後，都會優先讀取資料庫中資料以達成離線觀看的功能。縮圖則是透過 Glide 本身的本地暫存能力儲存，並未進入資料庫。

### 過濾戲劇名稱
戲劇列表頁面的工具列有個搜尋按鈕，點選後可以輸入戲劇名稱進行過濾，列表將只顯示包含該名稱的戲劇資料。當使用者關閉 APP 後，輸入的關鍵字將被保留在 shared preference 裡面，下次開啟 APP 時會從此處讀取關鍵字並自動進行過濾，以保留上次搜尋結果。

戲劇列表與關鍵字作為 LiveData，當資料更新後會在 view model 中處理，產生一個過濾後的新列表。此列表也是包覆在一個 LiveData 資料當中，一旦發生變化就會觸發 recyclerview adapter 與 UI 的更新。

### 透過網址開啟 APP
戲劇資訊頁面實作 deep link 功能，讓使用者可以透過特定網址開啟 APP 並顯示戲劇資訊。此一特定網址為
```
http://www.example.com/dramas/:id
```
此處的 :id 為某一特定戲劇的 id。例如使用 http://www.example.com/dramas/1 網址，即可開啟 APP 並顯示 id 為 1 的戲劇之資訊。

## 使用架構與套件
* 採用 MVVM 架構，使用 Android Architecture Components 中的 LiveData, ViewModel 與 Room
* 使用 Retrofit 下載資料，透過 gson 處理 json 格式文件
* 使用 Glide 下載及暫存圖片
* 使用 Android Data Binding Library
