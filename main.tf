# main.tf
terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.0"
    }
  }
}

provider "docker" {
  host = "unix:///var/run/docker.sock"
}

# Variables
variable "app_name" {
  type    = string
  default = "spring-app"
}

variable "image_tag" {
  type = string
}

variable "deployment_color" {
  type = string
}

variable "mysql_user" {
  type = string
}

variable "mysql_password" {
  type = string
}

variable "spring_datasource_url" {
  type = string
}

# Determine port based on color
locals {
  container_port = var.deployment_color == "blue" ? "8081" : "8082"
}

# Docker container resource
resource "docker_container" "spring_app" {
  name  = "${var.app_name}-${var.deployment_color}"
  image = var.image_tag

  ports {
    internal = 8081
    external = local.container_port
  }

  env = [
    "SPRING_DATASOURCE_URL=${var.spring_datasource_url}",
    "SPRING_DATASOURCE_USERNAME=${var.mysql_user}",
    "SPRING_DATASOURCE_PASSWORD=${var.mysql_password}"
  ]

  must_run = true
  restart  = "unless-stopped"

  healthcheck {
    test         = ["CMD", "curl", "-f", "http://localhost:8081/health"]
    interval     = "10s"
    timeout      = "5s"
    retries      = 3
    start_period = "10s"
  }
}

# Output the container ID and port
output "container_id" {
  value = docker_container.spring_app.id
}

output "container_port" {
  value = local.container_port
}
