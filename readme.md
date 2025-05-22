# 👥 User Explorer App

Este é um aplicativo Android desenvolvido em Kotlin que consome dados de uma API remota para exibir
uma lista de usuários e seus detalhes. O projeto utiliza **Clean Architecture**, **Jetpack Compose**, **Hilt**, **Room**, **Retrofit** e boas práticas de desenvolvimento moderno com foco em testabilidade e escalabilidade.

---

## 📱 Funcionalidades

- Listagem de usuários com nome e e-mail
- Pesquisa por nome de usuário
- Tela de detalhes com informações completas do usuário
- Cache local com fallback offline
- Estados de carregamento, erro e sucesso

---

## 🧱 Arquitetura

O projeto segue os princípios da **Clean Architecture**, separando responsabilidades em diferentes
camadas:

```pgsql
app/
│
├── data/
│   ├── local/               # Room (DAO, Database, Entidades)
│   ├── mappers/             # Mappers
│   ├── remote/              # Retrofit (ApiService, DTOs)
│   └── repository/          # UserRepositoryImpl.kt
│
├── domain/
│   ├── model/               # Modelo puro de domínio (User.kt)
│   ├── repository/          # Interface UserRepository.kt
│   └── usecase/             # UseCases individuais
│
├── presentation/
│   ├── userlist/
│   │   ├── UserListViewModel.kt
│   │   ├── UserListScreen.kt
│   │   └── components/
│   ├── userdetail/
│   │   ├── UserDetailViewModel.kt
│   │   └── UserDetailScreen.kt
│
├── di/                      # Módulos do Hilt
│
├── core/
│   └── util/                # Extensões, Resources
│
├── MainActivity.kt          # AndroidEntryPoint
├── MyApplication.kt         # HiltAndroidApp
└── NavRoutes.kt             # Rotas de Navegação
```

---

## 🛠 Tecnologias Utilizadas

- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Retrofit](https://square.github.io/retrofit/)
- [Room](https://developer.android.com/training/data-storage/room)
- [Coroutines &amp; Flow](https://kotlinlang.org/docs/flow.html)
- [Turbine](https://github.com/cashapp/turbine) para testes de Flow
- [JUnit](https://junit.org/) e [MockK](https://mockk.io/) para testes unitários

---

## ▶️ Como executar o projeto

1. Clone o repositório
2. Abra o projeto no Android Studio
3. Sincronize o Gradle e aguarde a indexação
4. Execute o projeto com um emulador ou dispositivo conectado