module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    devUpdate: {
      main: {
      }
    },
    connect: {
      server: {
        options: {
          port: 8000,
          hostname: '*',
          keepalive: true
        }
      }
    },
    clean: {
      dist: {
        src: ['dist']
      }
    },
    ngmin: {
      dist: {
        expand: true,
        cwd: 'src',
        src: ['app/**/*.js'],
        dest: 'dist'
      }
    },
    concat: {
      js: {
        options: {
          separator: ';'
        },
        src: ['src/lib/jquery.js', 'src/lib/underscore.js', 'src/lib/highcharts.js', 'src/lib/angular.js', 'src/lib/angular.*.js',
          'dist/app/app.js', 'dist/app/controller/*.js', 'dist/app/directive/*.js', 'dist/app/filter/*.js', 'dist/app/service/*.js'],
        dest: 'dist/home.js'
      },
      css: {
        src: ['src/style/*.css', 'dist/less.css'],
        dest: 'dist/home.css'
      }
    },
    less: {
      dist: {
        src: ['src/style/*.less'],
        dest: 'dist/less.css'
      }
    },
    cssmin: {
      dist: {
        src: 'dist/home.css',
        dest: 'dist/style/home.min.css'
      }
    },
    uglify: {
      dist: {
        src: 'dist/home.js',
        dest: 'dist/home.min.js'
      }
    },
    copy: {
      dist: {
        expand: true,
        cwd: 'src/',
        src: ['**', '!**/*.js', '!index.html', '!**/*.less', '!**/*.css'],
        dest: 'dist/'
      }
    },
    htmlrefs: {
      dist: {
        src: 'src/index.html',
        dest: 'dist/index.html'
      }
    },
    remove: {
      dist: {
        fileList: ['dist/home.css', 'dist/home.js', 'dist/less.css'],
        dirList: ['dist/app']
      }
    },
    cleanempty: {
      options: {
        files: false,
        folders: true
      },
      src: ['dist/**']
    },
    replace: {
      dist: {
        src: ['dist/home.min.js', 'dist/**/*.html'],
        overwrite: true,
        replacements: [{
          from: '../api',
          to: grunt.option('apiurl') || '../api'
        }]
      }
    }
  });

  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-cleanempty');
  grunt.loadNpmTasks('grunt-htmlrefs');
  grunt.loadNpmTasks('grunt-css');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-remove');
  grunt.loadNpmTasks('grunt-ngmin');
  grunt.loadNpmTasks('grunt-text-replace');
  grunt.loadNpmTasks('grunt-dev-update');
  grunt.loadNpmTasks('grunt-contrib-connect');

  // Default tasks.
  grunt.registerTask('default', ['clean', 'ngmin', 'concat:js', 'less', 'concat:css', 'cssmin',
    'uglify', 'copy', 'remove', 'cleanempty', 'htmlrefs', 'replace']);
};