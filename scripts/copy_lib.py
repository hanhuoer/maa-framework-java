import os
import zipfile
import shutil
import tempfile

script_dir = os.path.dirname(os.path.abspath(__file__))

project_dir = os.path.abspath(os.path.join(script_dir, '..'))
maa_libs_dir = os.path.join(project_dir, 'maa-libs')

platform_arch_map = {
    'MAA-linux-aarch64': 'maa-linux-aarch64/src/main/resources/linux_aarch64',
    'MAA-linux-x86_64': 'maa-linux-x86_64/src/main/resources/linux_x86_64',
    'MAA-macos-aarch64': 'maa-macos-aarch64/src/main/resources/mac_aarch64',
    'MAA-macos-x86_64': 'maa-macos-x86_64/src/main/resources/mac_x86_64',
    'MAA-win-aarch64': 'maa-windows-aarch64/src/main/resources/win_aarch64',
    'MAA-win-x86_64': 'maa-windows-x86_64/src/main/resources/win_x86_64'
}

def clear_all_directories(platform_arch_map):
    """
    清理所有目标目录中的内容，但保留 .gitkeep 文件
    """
    for target_dir in platform_arch_map.values():
        full_path = os.path.join(project_dir, target_dir)
        if os.path.exists(full_path):
            for item in os.listdir(full_path):
                item_path = os.path.join(full_path, item)
                if item == '.gitkeep':
                    continue
                if os.path.isfile(item_path) or os.path.islink(item_path):
                    os.unlink(item_path)
                elif os.path.isdir(item_path):
                    shutil.rmtree(item_path)
            print(f"清理目录 {full_path} 完成")

def extract_and_move(zip_files, platform_arch_map):
    """
    解压缩 zip 文件并移动 bin 目录内容到目标路径
    """
    for zip_file in zip_files:
        platform_arch = zip_file.split('-v')[0]  # 获取平台和架构标识
        if platform_arch in platform_arch_map:
            target_dir = os.path.join(project_dir, platform_arch_map[platform_arch])

            # 创建目标目录（如果不存在）
            os.makedirs(target_dir, exist_ok=True)

            # 解压缩 zip 文件
            zip_path = os.path.join(maa_libs_dir, zip_file)
            with zipfile.ZipFile(zip_path, 'r') as zip_ref:
                with tempfile.TemporaryDirectory() as temp_dir:
                    for member in zip_ref.namelist():
                        if member.startswith('bin/'):
                            zip_ref.extract(member, temp_dir)
                            extracted_path = os.path.join(temp_dir, member)
                            target_path = os.path.join(target_dir, os.path.basename(member))
                            if os.path.isdir(extracted_path):
                                shutil.copytree(extracted_path, target_path, dirs_exist_ok=True)
                            else:
                                shutil.copy2(extracted_path, target_path)

        print(f"解压并移动 {zip_file} 中的 bin 目录内容到 {target_dir}")

# 获取所有 zip 文件
zip_files = [f for f in os.listdir(maa_libs_dir) if f.endswith('.zip')]

# 清理所有目标目录
clear_all_directories(platform_arch_map)

# 执行复制操作
extract_and_move(zip_files, platform_arch_map)

print("所有文件已处理完毕")
