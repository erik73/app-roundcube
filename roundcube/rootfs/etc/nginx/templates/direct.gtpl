server {
    {{ if not .ssl }}
    listen 80 default_server;
    {{ else }}
    listen 80 default_server ssl;
    http2 on;
    {{ end }}

    include /etc/nginx/includes/server_params.conf;

    {{ if .ssl }}
    include /etc/nginx/includes/ssl_params.conf;

    ssl_certificate /ssl/{{ .certfile }};
    ssl_certificate_key /ssl/{{ .keyfile }};
    {{ end }}

location ~ ^/(README.md|INSTALL|LICENSE|CHANGELOG|UPGRADING)$ {
    deny all;
}

location ~ ^/(config|temp|logs)/ {
    deny all;
}

location ~ ^/(bin|SQL)/ {
    deny all;
}

    location ~ ^/static\.php(/.*)$ {
        include /etc/nginx/includes/fastcgi_params.conf;
        fastcgi_pass 127.0.0.1:9001;
        fastcgi_split_path_info ^(.+\.php)(/.+)$;
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
        fastcgi_param PATH_INFO $fastcgi_path_info;
    }

    location ~ .php$ {
        fastcgi_pass 127.0.0.1:9001;
        fastcgi_read_timeout 900;
        fastcgi_split_path_info ^(.+\.php)(/.+)$;
        fastcgi_index index.php;
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
        include /etc/nginx/includes/fastcgi_params.conf;
        fastcgi_param PATH_INFO $fastcgi_path_info;
    }
}
