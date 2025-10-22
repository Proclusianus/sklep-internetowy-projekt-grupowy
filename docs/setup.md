Dla aplikacji webowej:

Struktura projektu będzie wyglądać podobnie do tego wzoru (dokładna struktura aplikacji zostanie opisana po zakończeniu opracowywania wymagań projektu i utworzeniu jego podstaw).
sklep-aukcyjny-wielkie-akcje-i-transakcje/  
├── .git/  
├── .gitlab-ci.yml  
├── .gitignore  
├── README.md  
├── docs/  
│   └── (pliki dokumentacji)  
├── WebApp/  
│   ├── backend/  
│   │   ├── src/  
│   │   ├── tests/  
│   │   ├── config/  
│   │   ├── database/  
│   │   ├── node_modules/  
│   │   └── package.json / requirements.txt / pom.xml / composer.json  
│   ├── frontend/  
│   │   ├── public/  
│   │   ├── src/  
│   │   │   ├── components/  
│   │   │   ├── pages/  
│   │   │   ├── assets/  
│   │   │   └── services/  
│   │   ├── tests/  
│   │   ├── node_modules/  
│   │   └── package.json  
│   └── scripts/  
│        └── deploy.sh  
├── MobileApp/  
│   └── ...  
...  