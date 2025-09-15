# QBit: User Role Permissions

[![Version](https://img.shields.io/badge/version-0.30.0--SNAPSHOT-blue.svg)](https://github.com/Kingsrook/qbit-user-role-permissions)
[![License](https://img.shields.io/badge/license-GNU%20Affero%20GPL%20v3-green.svg)](https://www.gnu.org/licenses/agpl-3.0.en.html)
[![Java](https://img.shields.io/badge/java-17+-blue.svg)](https://adoptium.net/)
[![Maven Central](https://img.shields.io/maven-central/v/com.kingsrook.qbits/qbit-user-role-permissions.svg)](https://central.sonatype.com/namespace/com.kingsrook.qbits)

> **QBit Component for QQQ - Low-code Application Framework for Engineers**

This repository contains the **user role permissions QBit** that provides database tables and Java classes for managing application permissions for users and roles within the QQQ framework.

## 🚀 Overview

QBit User Role Permissions is a **backend component** that provides the database schema and Java classes needed to implement user authentication and authorization systems in QQQ applications. It's designed to work seamlessly with the QQQ framework's metadata-driven architecture.

### What This Repository Contains

- **Database Schema**: SQL tables for users, roles, and permissions
- **Java Classes**: Data models and utilities for permission management
- **QBit Integration**: Seamless integration with QQQ's QBit system
- **Testing Support**: Test database setup and validation

### What This Repository Does NOT Contain

- **QQQ Server**: The actual low-code engine and backend
- **Core Framework**: Business logic, process engine, or data management
- **Frontend Components**: User interface or dashboard components
- **Authentication Logic**: Login/logout or session management

## 🏗️ Architecture

### Technology Stack

- **Java Framework**: QQQ Backend Core integration
- **Database**: RDBMS module with H2 for testing
- **Build System**: Maven with QQQ BOM dependency management
- **Testing**: JUnit 5 with AssertJ for assertions
- **Code Quality**: Checkstyle configuration included

### Core Dependencies

- **qqq-backend-core**: Core QQQ framework functionality
- **qqq-backend-module-rdbms**: Database module for RDBMS operations
- **H2 Database**: In-memory database for testing
- **JUnit 5**: Testing framework
- **AssertJ**: Fluent assertions for testing

## 📁 Project Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── kingsrook/
│               └── qbits/
│                   └── userrolepermissions/
│                       ├── User.java              # User data model
│                       ├── Role.java               # Role data model
│                       ├── Permission.java         # Permission data model
│                       ├── UserRole.java           # User-Role relationship
│                       ├── RolePermission.java     # Role-Permission relationship
│                       └── ...                     # Additional classes
└── test/
    ├── java/
    │   └── com/
    │       └── kingsrook/
    │           └── qbits/
    │               └── userrolepermissions/
    │                   └── ...                     # Test classes
    └── resources/
        └── test-database.sql                       # Test database schema
```

## 🎯 QBit Functionality

### What This QBit Provides

- **User Management**: User data models and database tables
- **Role Management**: Role-based access control (RBAC) support
- **Permission System**: Granular permission management
- **Relationship Mapping**: User-role and role-permission relationships
- **Database Schema**: Complete SQL schema for permission systems
- **Java Models**: Type-safe Java classes for all entities

### What This QBit Does NOT Do

- **Authentication**: Login/logout or session management
- **Authorization Logic**: Permission checking or access control
- **User Interface**: Frontend components or UI elements
- **Business Logic**: Application-specific permission rules

## 🚀 Getting Started

### Prerequisites

- **Java 17+** (required for QQQ features)
- **Maven 3.8+** (for build system)
- **QQQ Framework**: Understanding of QQQ's QBit system

### Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/Kingsrook/qbit-user-role-permissions.git
   cd qbit-user-role-permissions
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run tests**
   ```bash
   mvn test
   ```

### Use in Your QQQ Project

Add this QBit to your QQQ application's `pom.xml`:

```xml
<dependency>
    <groupId>com.kingsrook.qbits</groupId>
    <artifactId>qbit-user-role-permissions</artifactId>
    <version>0.30.0-SNAPSHOT</version>
</dependency>
```

### Database Setup

The QBit provides SQL schema files that can be used to set up your database:

```sql
-- Include the schema from src/main/resources or test-database.sql
-- This will create the necessary tables for user role permissions
```

## 📖 Detailed Documentation

For comprehensive development information, see:
- **[QQQ Wiki](https://github.com/Kingsrook/qqq.wiki)** - Complete QQQ framework documentation
- **[QBit Development](https://github.com/Kingsrook/qqq.wiki/QBit-Development)** - QBit development guide

## 🔧 Configuration

### Database Configuration

This QBit works with any RDBMS supported by QQQ's RDBMS module:

- **PostgreSQL**: Production-ready database support
- **MySQL**: Enterprise database support  
- **H2**: Development and testing database
- **SQLite**: Lightweight database option

### QQQ Integration

This QBit integrates with QQQ's metadata system:

- **Automatic Discovery**: QQQ automatically discovers QBit classes
- **Schema Management**: Database schema managed through QQQ
- **Type Safety**: Java classes provide compile-time type checking

## 🧪 Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=UserRolePermissionsTest
```

### Test Structure

- **Unit Tests**: Individual class testing
- **Integration Tests**: Database interaction testing
- **Schema Tests**: Database schema validation
- **Mock Data**: Test data and fixtures

## 📦 Building for Production

### Production Build

```bash
mvn clean package
```

The build process:
- Compiles Java source code
- Runs all tests with database validation
- Packages JAR with QBit classes
- Generates Maven artifacts for distribution

### Deployment

This QBit is deployed as part of your QQQ application:

- **JAR Dependency**: Included in your application's classpath
- **Database Schema**: Applied to your production database
- **QBit Registration**: Automatically registered with QQQ framework

## 🔐 Security Considerations

### Data Protection

- **Sensitive Data**: User credentials should be hashed before storage
- **Access Control**: Implement proper authorization checks
- **Audit Logging**: Log permission changes for security auditing

### Best Practices

- **Principle of Least Privilege**: Grant minimum necessary permissions
- **Regular Audits**: Review user roles and permissions regularly
- **Secure Defaults**: Use secure default permission configurations

## 🤝 Contributing

**Important**: This repository is a component of the QQQ framework. All contributions, issues, and discussions should go through the main QQQ repository.

### Development Workflow

1. **Fork the main QQQ repository**: https://github.com/Kingsrook/qqq
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Make your changes** (including QBit changes if applicable)
4. **Run tests**: `mvn test`
5. **Commit your changes**: `git commit -m 'Add amazing feature'`
6. **Push to the branch**: `git push origin feature/amazing-feature`
7. **Open a Pull Request** to the main QQQ repository

### Code Standards

- **Java**: Follow QQQ's coding standards
- **Testing**: Maintain high test coverage
- **Documentation**: Update relevant documentation
- **Checkstyle**: Follow included checkstyle configuration

## 📄 License

This project is licensed under the GNU Affero General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

```
QBit User Role Permissions
Copyright (C) 2021-2022 Kingsrook, LLC
651 N Broad St Ste 205 # 6917 | Middletown DE 19709 | United States
contact@kingsrook.com | https://github.com/Kingsrook/
```

**Note**: This is a component of the QQQ framework. For the complete license and more information, see the main QQQ repository: https://github.com/Kingsrook/qqq

## 🆘 Support & Community

### ⚠️ Important: Use Main QQQ Repository

**All support, issues, discussions, and community interactions should go through the main QQQ repository:**

- **Main Repository**: https://github.com/Kingsrook/qqq
- **Issues**: https://github.com/Kingsrook/qqq/issues
- **Discussions**: https://github.com/Kingsrook/qqq/discussions
- **Wiki**: https://github.com/Kingsrook/qqq.wiki

### Why This Repository Exists

This repository is maintained separately from the main QQQ repository to:
- **Enable independent QBit development** and versioning
- **Allow QBit-specific CI/CD** and deployment pipelines
- **Provide clear separation** between QBit and core framework concerns
- **Support different release cycles** for QBits vs. core framework

### Getting Help

- **Documentation**: Check the [QQQ Wiki](https://github.com/Kingsrook/qqq.wiki)
- **Issues**: Report bugs and feature requests on [Main QQQ Issues](https://github.com/Kingsrook/qqq/issues)
- **Discussions**: Join community discussions on [Main QQQ Discussions](https://github.com/Kingsrook/qqq/discussions)
- **Questions**: Ask questions in the main QQQ repository

### Contact Information

- **Company**: Kingsrook, LLC
- **Email**: contact@kingsrook.com
- **Website**: https://kingsrook.com
- **Main GitHub**: https://github.com/Kingsrook/qqq

## 🙏 Acknowledgments

- **QQQ Framework Team**: For the underlying low-code platform
- **Java Community**: For the robust Java ecosystem
- **Open Source Community**: For the tools and libraries that make this possible

---

**Built with ❤️ by the Kingsrook Team**

**This is a QBit component of the QQQ framework. For complete information, support, and community, visit: https://github.com/Kingsrook/qqq**

